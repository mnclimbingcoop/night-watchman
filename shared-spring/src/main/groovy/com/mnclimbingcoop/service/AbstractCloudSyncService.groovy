package com.mnclimbingcoop.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.DeleteMessageRequest
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.amazonaws.services.sqs.model.SendMessageResult
import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.compresssion.StringCompressor
import com.mnclimbingcoop.config.AwsConfiguration
import com.mnclimbingcoop.observables.MessageObservableFactory

import org.apache.http.NoHttpResponseException

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentSkipListSet

import javax.annotation.PostConstruct

import rx.Observable

@CompileStatic
@Slf4j
abstract class AbstractCloudSyncService<T,R> {

    static final int MAX_TRIES = 5

    protected final ObjectMapper objectMapper
    protected final HealthService healthService

    AWSCredentialsProvider credentialsProvider

    protected String pushQueueUrl
    protected String pullQueueUrl
    protected boolean flushCommands = false
    protected final Integer maxNumberOfMessages = 10

    protected final String region
    protected final String pushQueue
    protected final String pullQueue

    protected AmazonSQS sqs
    protected AwsService awsService

    Set<String> received = new ConcurrentSkipListSet<String>()

    static final Long MAX_DATA_BYTES = 1024 * 256

    AbstractCloudSyncService(String region,
                             String pushQueue,
                             String pullQueue,
                             HealthService healthService,
                             ObjectMapper objectMapper) {

        this.objectMapper = objectMapper
        this.healthService = healthService
        this.pushQueue = pushQueue
        this.pullQueue = pullQueue
        this.region = region
        this.awsService = new AwsService(region)

    }

    AbstractCloudSyncService(AwsConfiguration awsConfig,
                             HealthService healthService,
                             ObjectMapper objectMapper) {

        this.objectMapper = objectMapper
        this.healthService = healthService
        this.pushQueue = awsConfig.sqs.pushQueue
        this.pullQueue = awsConfig.sqs.pullQueue
        this.region = awsConfig.region
        this.awsService = new AwsService(awsConfig.region)
    }

    @PostConstruct
    void setup() {
        assert region
        assert pullQueue || pushQueue

        credentialsProvider = new DefaultAWSCredentialsProviderChain()
        sqs = awsService.getSqsClient()
        createQueues()
    }

    protected void createQueues() {
        pushQueueUrl = createQueue(pushQueue)
        pullQueueUrl = createQueue(pullQueue)
        log.info "Using SQS queues: push=${pushQueueUrl} pull=${pullQueueUrl}"
    }

    protected String createQueue(String queueName) {
        String queueUrl = null
        while (queueName && !queueUrl) {
            try {
                CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName)
                queueUrl = sqs.createQueue(createQueueRequest).queueUrl
            } catch (AmazonServiceException ex) {
                log.error 'error creating SQS queue {}', ex
                log.warn "retrying in 5 seconds"
                Thread.sleep(5000)
                log.warn "Re-authenticating to AWS"
                sqs = awsService.getSqsClient()
            }
        }
        return queueUrl
    }

    /** Push a message to SQS */
    SendMessageResult sendSqsMessage(T send) {
        if (!pushQueueUrl) {
            log.error "No PUSH queue defined."
            return null
        }
        String payload = objectMapper.writeValueAsString(send)
        String gzipped = StringCompressor.compress(payload)
        Long payloadSize = gzipped.size()
        if (payloadSize >= MAX_DATA_BYTES) {
            log.warn "payload size=${payloadSize} exceeds the ${MAX_DATA_BYTES} byte maximum " +
                     "size for an SQS message! (it will probably fail)"
        }
        SendMessageResult result = sendMessage(gzipped)
        if (result) {
            healthService.sentMessage()
            log.info "sent ${payloadSize} byte message=${result.messageId} data to SQS queue"
            return result
        } else {
            log.error "Failed to send ${payloadSize} byte message: ${payload}"
            healthService.sendMessageFailed()
        }
        return null
    }

    protected SendMessageResult sendMessage(String message) {
        int MAX_TRIES = 5
        int tried = 0
        SendMessageResult result
        while (!result && tried < MAX_TRIES) {
            try {
                result = sqs.sendMessage(new SendMessageRequest(pushQueueUrl, message))
            } catch ( NoHttpResponseException | AmazonServiceException ex) {
                log.error 'Error sending SQS message {}', ex
                Thread.sleep(5000)
                sqs = awsService.getSqsClient()
            }
            tried++

            if (tried > 1) {
                if (result) {
                    log.info "Resend / Reauthenticatiion Successful."
                } else {
                    log.error 'Gave up sending message. Max retries reached {}', MAX_TRIES
                }
            }
        }
        return result
    }

    /** Returns an obserable that streams SQS messages */
    Observable<R> getObservable() {
        MessageObservableFactory messageFactory = new MessageObservableFactory(
            awsService, healthService, pullQueueUrl, maxNumberOfMessages
        )
        messageFactory.getSqsObservable().distinct{ Message message ->
            return message.messageId
        }.map{ Message message ->
            log.info "Received Message id=${message.messageId} md5=${message.getMD5OfBody()}"
            String json = StringCompressor.decompress(message.body)
            R item =  convert(json)
            if (flushCommands) {
                String messageRecieptHandle = message.receiptHandle
                sqs.deleteMessage(new DeleteMessageRequest(pullQueueUrl, messageRecieptHandle))
            }
            return item
        }
    }

    // return objectMapper.readValue(json, R)
    abstract R convert(String data)

}
