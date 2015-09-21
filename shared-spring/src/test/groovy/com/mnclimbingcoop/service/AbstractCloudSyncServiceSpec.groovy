package com.mnclimbingcoop.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.CreateQueueResult
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageResult
import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.compresssion.StringCompressor

import spock.lang.Specification

class AbstractCloudSyncServiceSpec extends Specification {

    static final String REGION = 'REGION'
    static final String PUSH_QUEUE = 'TEST_PUSH'
    static final String PULL_QUEUE = 'TEST_PULL'

    HealthService healthService
    ObjectMapper objectMapper
    AwsService awsService
    AmazonSQS sqs
    Message message
    ReceiveMessageResult result

    AbstractCloudSyncService service

    void setup() {
        objectMapper = new ObjectMapper()

        healthService = Mock()
        awsService = Mock()
        sqs = Mock()
        message = Mock()
        result = Mock()

        service = new TestCloudSyncService(healthService, objectMapper)
        service.pushQueueUrl = "FAKE_PUSH_URL"
        service.pullQueueUrl = "FAKE_PULL_URL"

        service.awsService = awsService
    }

    /* Testing this stacktrace:
     *
     *  2015-09-02 12:40:26.366 ERROR 7 --- [      Thread-14] c.m.service.HeartBeatService             : Error while heartbeat from night watchman The security token included in the request is expired (Service: AmazonSQS; Status Code: 403; Error Code: ExpiredToken; Request ID: 4271af2e-355f-5e23-9afe-e14ec52574f8)
     *  com.amazonaws.AmazonServiceException: The security token included in the request is expired (Service: AmazonSQS; Status Code: 403; Error Code: ExpiredToken; Request ID: 4271af2e-355f-5e23-9afe-e14ec52574f8)
     *      at com.amazonaws.http.AmazonHttpClient.handleErrorResponse(AmazonHttpClient.java:1160)
     *      at com.amazonaws.http.AmazonHttpClient.executeOneRequest(AmazonHttpClient.java:748)
     *      at com.amazonaws.http.AmazonHttpClient.executeHelper(AmazonHttpClient.java:467)
     *      at com.amazonaws.http.AmazonHttpClient.execute(AmazonHttpClient.java:302)
     *      at com.amazonaws.services.sqs.AmazonSQSClient.invoke(AmazonSQSClient.java:2419)
     *      at com.amazonaws.services.sqs.AmazonSQSClient.deleteMessage(AmazonSQSClient.java:1472)
     *      at com.mnclimbingcoop.service.AbstractCloudSyncService$_getObservable_closure2.doCall(AbstractCloudSyncService.groovy:181)
     *      at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     *      at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     *      at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     *      at java.lang.reflect.Method.invoke(Method.java:497)
     *      at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90)
     *      at groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:324)
     *      at org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:292)
     *      at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1016)
     *      at groovy.lang.Closure.call(Closure.java:423)
     *      at org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:51)
     *      at org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:103)
     *      at com.sun.proxy.$Proxy53.call(Unknown Source)
     *      at rx.internal.operators.OperatorMap$1.onNext(OperatorMap.java:55)
     *      at rx.internal.operators.OperatorDistinct$1.onNext(OperatorDistinct.java:63)
     *      at com.mnclimbingcoop.observables.MessageObservableFactory$_getSqsObservable_closure1$_closure2.doCall(MessageObservableFactory.groovy:57)
     *      at com.mnclimbingcoop.observables.MessageObservableFactory$_getSqsObservable_closure1$_closure2.call(MessageObservableFactory.groovy)
     *      at groovy.lang.Closure.run(Closure.java:504)
     *      at java.lang.Thread.run(Thread.java:745)
     *  Caused by: rx.exceptions.OnErrorThrowable$OnNextValue: OnError while emitting onNext value: com.amazonaws.services.sqs.model.Message.class
     *      at rx.exceptions.OnErrorThrowable.addValueAsLastCause(OnErrorThrowable.java:104)
     *      at rx.internal.operators.OperatorMap$1.onNext(OperatorMap.java:58)
     *      ... 5 common frames omitted
     */
    void 'getting messages and deleting handles errors'() {
        given:
        Integer limitMessages = 5
        final String body = StringCompressor.compress('{ "foo": "bar" }')

        when:
        service.getObservable().limit(limitMessages).toBlocking().toIterable().eachWithIndex{ Map map, int i ->
            println "receiving message # ${i}."
        }


        then:
        4 * awsService.getSqsClient() >> sqs
        _ * sqs.receiveMessage(_) >> result
        _ * result.getMessages() >> (1..5).collect{ message }
        _ * healthService.checkedMessages(5)
        _ * message.getMessageId() >> { UUID.randomUUID().toString() }
        _ * message.getMD5OfBody() >> 'MD5'
        _ * message.getReceiptHandle() >> 'RECEIPT_HANDLE'
        _ * message.getBody() >> body
        1 * sqs.deleteMessage(_)
        2 * sqs.deleteMessage(_) >> { throw new AmazonServiceException('EXPIRED') }
        4 * sqs.deleteMessage(_)
        0 * _

    }

    /* From Stacktrace:
     *  com.amazonaws.AmazonServiceException: The security token included in the request is expired (Service: AmazonSQS; Status Code: 403; Error Code: ExpiredToken; Request ID: 043ede7a-5225-5fe9-89a8-901a9f035573)
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
    void 'will try to create queue until it succeeds'() {
        given:
        CreateQueueResult result = Mock()

        when:
        service.createQueues()

        then:
        3 * awsService.getSqsClient() >> sqs
        1 * sqs.createQueue(_) >> { throw new AmazonServiceException('EXPIRED') }
        1 * sqs.createQueue(_) >>  result
        1 * sqs.createQueue(_) >>  result
        2 * result.getQueueUrl() >> 'sqs://queue'
        0 * _

    }

    static class TestCloudSyncService extends AbstractCloudSyncService<Map, Map> {

        TestCloudSyncService(HealthService healthService, ObjectMapper objectMapper) {
            super(REGION, PUSH_QUEUE, PULL_QUEUE, healthService, objectMapper)
        }

        @Override
        Map convert(String data) {
            return objectMapper.readValue(data, Map)
        }
    }

}
