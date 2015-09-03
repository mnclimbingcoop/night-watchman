package com.mnclimbingcoop.observables

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageResult
import com.mnclimbingcoop.service.AwsService
import com.mnclimbingcoop.service.HealthService

import spock.lang.Specification

class MessageObservableFactorySpec extends Specification {

    MessageObservableFactory factory
    AwsService awsService
    HealthService healthService
    AmazonSQS sqs
    static final String QUEUE_URL = 'sqs://queue'
    static final Integer MAX_MESSAGES = 10
    static final Integer _MESSAGES = 10
    Message message
    ReceiveMessageResult result

    void setup() {
        healthService = Mock()
        awsService = Mock()
        message = Mock()
        result = Mock()
        sqs = Mock()
        factory = new MessageObservableFactory(awsService, healthService, QUEUE_URL, MAX_MESSAGES)

    }

    /* From Stacktrace:

     *  2015-09-02 10:09:43.052 ERROR 7 --- [      Thread-13] c.m.o.MessageObservableFactory           : error creading from SQS queue {}

     *  com.amazonaws.AmazonServiceException: The security token included in the request is expired (Service: AmazonSQS; Status Code: 403; Error Code: ExpiredToken; Request ID: 0f81e73d-59a9-54be-a765-a95d948ec786)
     *      at com.amazonaws.http.AmazonHttpClient.handleErrorResponse(AmazonHttpClient.java:1160)
     *      at com.amazonaws.http.AmazonHttpClient.executeOneRequest(AmazonHttpClient.java:748)
     *      at com.amazonaws.http.AmazonHttpClient.executeHelper(AmazonHttpClient.java:467)
     *      at com.amazonaws.http.AmazonHttpClient.execute(AmazonHttpClient.java:302)
     *      at com.amazonaws.services.sqs.AmazonSQSClient.invoke(AmazonSQSClient.java:2419)
     *      at com.amazonaws.services.sqs.AmazonSQSClient.receiveMessage(AmazonSQSClient.java:1130)
     *      at com.mnclimbingcoop.observables.MessageObservableFactory$_getSqsObservable_closure1$_closure2.doCall(MessageObservableFactory.groovy:54)
     *      at com.mnclimbingcoop.observables.MessageObservableFactory$_getSqsObservable_closure1$_closure2.call(MessageObservableFactory.groovy)
     *      at groovy.lang.Closure.run(Closure.java:504)
     *      at java.lang.Thread.run(Thread.java:745)
    */

    void 'handles connection exceptions'() {
        given:
        Integer limitMessages = 35
        boolean finished = false

        when:
        factory.getSqsObservable().limit(limitMessages).toBlocking().toIterable().eachWithIndex{ Message msg, int i ->
            println "receiving message # ${i}."
        }

        then: 'SQS client is fetched'
        3 * awsService.getSqsClient() >> sqs

        and: 'The security token is expired'
        1 * sqs.receiveMessage(_) >> { throw new AmazonServiceException('EXPIRED') }
        1 * healthService.checkMessagesFailed()

        and: 'messages are returned'
        1 * sqs.receiveMessage(_) >> result
        1 * result.getMessages() >> (1..MAX_MESSAGES).collect{ message }

        and: 'The security token is expired'
        1 * sqs.receiveMessage(_) >> { throw new AmazonServiceException('EXPIRED') }
        1 * healthService.checkMessagesFailed()

        and: 'no messages to return'
        2 * sqs.receiveMessage(_) >> result
        2 * result.getMessages() >> (1..MAX_MESSAGES).collect{ message }

        1 * sqs.receiveMessage(_) >> result
        1 * result.getMessages() >> (1..6).collect{ message }
        3 * healthService.checkedMessages(MAX_MESSAGES)

        and: 'there is a little bit of give here since this is an async observable'
        (0..1) * healthService.checkedMessages(5)
        0 * _

    }

}
