package com.mnclimbingcoop.observables

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.mnclimbingcoop.service.AwsService
import com.mnclimbingcoop.service.HealthService

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.apache.http.NoHttpResponseException

import rx.Observable
import rx.Subscriber

@CompileStatic
@Slf4j
class MessageObservableFactory {

    final String queueUrl
    final Integer maxNumberOfMessages
    protected final HealthService healthService
    protected final AwsService awsService
    protected int connectionRefreshMs = 5000
    protected int moreMessagesMs = 100
    protected int noMessagesMs = 1500
    protected boolean shutdown = false

    MessageObservableFactory(String queueUrl) {
        this.queueUrl = queueUrl
        this.maxNumberOfMessages = 10
    }

    MessageObservableFactory(AwsService awsService,
                             HealthService healthService,
                             String queueUrl,
                             Integer maxNumberOfMessages) {
        this.healthService = healthService
        this.maxNumberOfMessages = maxNumberOfMessages
        this.queueUrl = queueUrl
        this.awsService = awsService
    }

    Observable<Message> getSqsObservable() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        receiveMessageRequest.maxNumberOfMessages = maxNumberOfMessages
        log.trace "checking for messages at ${queueUrl} (max ${maxNumberOfMessages})"
        return Observable.create({ Subscriber<Message> subscriber ->
            AmazonSQS sqs = awsService.getSqsClient()

            while(!subscriber.unsubscribed) {

                int lastPass = 0
                boolean lastCheckErrored = false
                try {
                    // if last attempt failed, reconnect.
                    for (Message message : sqs.receiveMessage(receiveMessageRequest).messages) {
                        log.info "Received Message id=${message.messageId} md5=${message.getMD5OfBody()}"
                        if (subscriber.unsubscribed) { break }
                        subscriber.onNext(message)
                        lastPass++
                    }
                    healthService.checkedMessages(lastPass)
                } catch (SocketTimeoutException | NoHttpResponseException | AmazonServiceException ex) {
                    log.error 'error checking messages from SQS queue', ex
                    Thread.sleep(connectionRefreshMs)
                    lastCheckErrored = true
                    healthService.checkMessagesFailed()
                    log.error 'Refreshing credentials and getting new AWS SQS client.'
                    sqs = awsService.getSqsClient()
                }
                if (lastPass) {
                    Thread.sleep(moreMessagesMs)
                } else {
                    Thread.sleep(noMessagesMs)
                }

                // This stream never ends as long as the subscriber is subscribed
                if (shutdown) { subscriber.unsubscribe() }
            }
        } as Observable.OnSubscribe<Message>)
    }
}
