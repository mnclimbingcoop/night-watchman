package com.mnclimbingcoop.observables

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.mnclimbingcoop.service.HealthService

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import rx.Observable
import rx.Subscriber

@CompileStatic
@Slf4j
class MessageObservableFactory {

    final String queueUrl
    final Integer maxNumberOfMessages
    protected final AmazonSQS sqs
    protected final HealthService healthService

    MessageObservableFactory(String queueUrl) {
        this.queueUrl = queueUrl
        this.maxNumberOfMessages = 10
    }

    MessageObservableFactory(AmazonSQS sqs,
                             HealthService healthService,
                             String queueUrl,
                             Integer maxNumberOfMessages) {
        this.sqs = sqs
        this.healthService = healthService
        this.queueUrl = queueUrl
        this.maxNumberOfMessages = maxNumberOfMessages
    }

    Observable<Message> getSqsObservable() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        receiveMessageRequest.maxNumberOfMessages = maxNumberOfMessages
        log.trace "checking for messages at ${queueUrl} (max ${maxNumberOfMessages})"
        return Observable.create({ Subscriber<Message> subscriber ->
            Thread.start {
                while(!subscriber.unsubscribed) {
                    int lastPass = 0
                    for (Message message : sqs.receiveMessage(receiveMessageRequest).messages) {
                        log.info "Received Message id=${message.messageId} md5=${message.getMD5OfBody()}"
                        if (subscriber.unsubscribed) { break }
                        subscriber.onNext(message)
                        lastPass++
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
