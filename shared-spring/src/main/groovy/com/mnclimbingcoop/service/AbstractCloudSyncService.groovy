package com.mnclimbingcoop.service

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

    protected final ObjectMapper objectMapper
    protected final HealthService healthService
    protected AmazonSQS sqs
    protected String pushQueueUrl
    protected String pullQueueUrl
    protected boolean flushCommands = false
    protected Integer maxNumberOfMessages = 10

    protected String region
    protected String pushQueue
    protected String pullQueue

    Set<String> received = new ConcurrentSkipListSet<String>()

    static final Long MAX_DATA_BYTES = 1024 * 256

    AbstractCloudSyncService(String region,
                             String pushQueue,
                             HealthService healthService,
                             ObjectMapper objectMapper) {

        this.objectMapper = objectMapper
        this.healthService = healthService
        this.pushQueue = pushQueue
        this.region = region

    }

    AbstractCloudSyncService(AwsConfiguration awsConfig,
                             HealthService healthService,
                             ObjectMapper objectMapper) {

        this.objectMapper = objectMapper
        this.healthService = healthService
        this.pushQueue = awsConfig.sqs.pushQueue
        this.pullQueue = awsConfig.sqs.pullQueue
        this.region = awsConfig.region
    }

    @PostConstruct
    void setup() {
        AWSCredentialsProvider credProvider = new DefaultAWSCredentialsProviderChain()
        AWSCredentials creds = credProvider.credentials
        sqs = new AmazonSQSClient(creds)
        sqs.region = Region.getRegion(Regions.fromName(region))

        assert region
        assert pullQueue || pushQueue

        createQueues()
    }

    protected void createQueues() {
        if (pushQueue) {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(pushQueue)
            pushQueueUrl = sqs.createQueue(createQueueRequest).queueUrl
        }
        if (pullQueue) {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(pullQueue)
            pullQueueUrl = sqs.createQueue(createQueueRequest).queueUrl
        }

        log.info "Using SQS queues: push=${pushQueueUrl} pull=${pullQueueUrl}"
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
        try {
            SendMessageResult result = sqs.sendMessage(new SendMessageRequest(pushQueueUrl, gzipped))
            healthService.sentMessage()
            log.info "sent ${payloadSize} byte message=${result.messageId} data to SQS queue"
            return result
        } catch ( NoHttpResponseException ex) {
            log.error "Failed to send ${payloadSize} byte message: ${payload}"
            healthService.sendMessageFailed()
        }
        return null
    }

    /** Returns an obserable that streams SQS messages */
    Observable<R> getObservable() {
        MessageObservableFactory messageFactory = new MessageObservableFactory(sqs, healthService, pullQueueUrl, maxNumberOfMessages)
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
