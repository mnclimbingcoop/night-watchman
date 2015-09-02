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
            Thread.start {

                AmazonSQS sqs = awsService.getSqsClient()

                while(!subscriber.unsubscribed) {
                    int lastPass = 0
                    try {
                        for (Message message : sqs.receiveMessage(receiveMessageRequest).messages) {
                            log.info "Received Message id=${message.messageId} md5=${message.getMD5OfBody()}"
                            if (subscriber.unsubscribed) { break }
                            subscriber.onNext(message)
                            lastPass++
                        }
                    } catch (SocketTimeoutException | NoHttpResponseException | AmazonServiceException ex) {
                        log.error 'error creading from SQS queue {}', ex
                        Thread.sleep(5000)
                        sqs = awsService.getSqsClient()
                    }
                    healthService.checkedMessages(lastPass)
                    if (lastPass) {
                        Thread.sleep(100)
                    } else {
                        Thread.sleep(1500)
                    }
                    // This stream never ends as long as the subscriber is subscribed
                }
            }
        } as Observable.OnSubscribe<Message>)
    }
}
