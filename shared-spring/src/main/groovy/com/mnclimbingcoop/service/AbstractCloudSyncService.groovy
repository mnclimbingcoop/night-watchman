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
import rx.schedulers.Schedulers

@CompileStatic
@Slf4j
abstract class AbstractCloudSyncService<T,R> {

    protected final ObjectMapper objectMapper
    protected final HealthService healthService

    AWSCredentialsProvider credentialsProvider

    protected String pushQueueUrl
    protected String pullQueueUrl
    protected final Integer maxNumberOfMessages = 10

    protected final String region
    protected final String pushQueue
    protected final String pullQueue

    protected AwsService awsService
    protected boolean quiet = false

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
        createQueues()
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
        SendMessageResult result = sendMessageWithRetry(gzipped)
        if (result) {
            healthService.sentMessage()
            logMessage "Sent ${payloadSize}b message=${result.messageId} to=", pushQueueUrl
            return result
        } else {
            log.error "Failed to send ${payloadSize} byte message: ${payload}"
            healthService.sendMessageFailed()
        }
        return null
    }


    /** Returns an observable that streams SQS messages */
    Observable<R> getObservable() {
        AwsRetryService awsRetry = new AwsRetryService<Boolean>(awsService)
        MessageObservableFactory messageFactory = new MessageObservableFactory(
            awsService, healthService, pullQueueUrl, maxNumberOfMessages
        )
        messageFactory.getSqsObservable().distinct{ Message message ->
            return message.messageId
        }.map{ Message message ->
            logMessage "Received message=${message.messageId} from=", pullQueueUrl
            String json = StringCompressor.decompress(message.body)
            R item =  convert(json)

            DeleteMessageRequest request = new DeleteMessageRequest(pullQueueUrl, message.receiptHandle)
            awsRetry.withRetry('deleting SQS message', 5) { AmazonSQS sqs ->
                sqs.deleteMessage(request)
                return true
            }

            return item
        }.onBackpressureBuffer()
    }

    protected void createQueues() {
        pushQueueUrl = createQueue(pushQueue)
        pullQueueUrl = createQueue(pullQueue)
        log.info "Using SQS queues: push=${pushQueueUrl} pull=${pullQueueUrl}"
    }

    protected SendMessageResult sendMessageWithRetry(String message) {
        SendMessageRequest request = new SendMessageRequest(pushQueueUrl, message)
        AwsRetryService awsRetry = new AwsRetryService<SendMessageResult>(awsService)
        return awsRetry.withRetry('sending SQS message', 5) { AmazonSQS sqs ->
            sqs.sendMessage(request)
        }
    }

    protected String createQueue(String queueName) {
        if (!queueName) { return null }
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName)
        AwsRetryService awsRetry = new AwsRetryService<String>(awsService)
        return awsRetry.withRetry('creating SQS queue') { AmazonSQS sqs ->
            sqs.createQueue(createQueueRequest).queueUrl
        }
    }

    String getQueueName(String queueUrl) {
        if (!queueUrl) { return null }
        queueUrl.replaceFirst('https://sqs.us-east-1.amazonaws.com/', '')
    }

    protected void logMessage(GString str, String queueUrl) {
        String queueName = getQueueName(queueUrl)
        if (quiet) {
            log.debug str + queueName
        } else {
            log.info str + queueName
        }
    }

    // return objectMapper.readValue(json, R)
    abstract R convert(String data)

}
