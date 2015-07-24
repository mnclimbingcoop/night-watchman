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

import java.util.concurrent.ConcurrentSkipListSet

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct

@CompileStatic
@Slf4j
abstract class AbstractCloudSyncService<T,R> {

    protected final ObjectMapper objectMapper
    protected final AwsConfiguration awsConfig
    protected AmazonSQS sqs
    protected String pushQueueUrl
    protected String pullQueueUrl
    protected boolean flushCommands = false
    protected Integer maxNumberOfMessages = 10

    Set<String> received = new ConcurrentSkipListSet<String>()

    static final Long MAX_DATA_BYTES = 1024 * 256

    AbstractCloudSyncService(ObjectMapper objectMapper,
                             AwsConfiguration awsConfig) {
        this.objectMapper = objectMapper
        this.awsConfig = awsConfig
    }

    @PostConstruct
    void setup() {
        AWSCredentialsProvider credProvider = new DefaultAWSCredentialsProviderChain()
        AWSCredentials creds = credProvider.credentials
        sqs = new AmazonSQSClient(creds)
        sqs.region = Region.getRegion(Regions.fromName(awsConfig.region))

        assert awsConfig.sqs.pushQueue != null
        assert awsConfig.sqs.pullQueue != null

        createQueue()
    }

    protected void createQueue() {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(awsConfig.sqs.pushQueue)
        pushQueueUrl = sqs.createQueue(createQueueRequest).queueUrl

        createQueueRequest = new CreateQueueRequest(awsConfig.sqs.pullQueue)
        pullQueueUrl = sqs.createQueue(createQueueRequest).queueUrl

        log.info "Using SQS queues: push=${pushQueueUrl} pull=${pullQueueUrl}"
    }

    SendMessageResult sendSqsMessage(T send) {
        String payload = objectMapper.writeValueAsString(send)
        String gzipped = StringCompressor.compress(payload)
        Long payloadSize = gzipped.size()
        if (payloadSize >= MAX_DATA_BYTES) {
            log.warn "payload size=${payloadSize} exceeds the ${MAX_DATA_BYTES} byte maximum " +
                     "size for an SQS message! (it will probably fail)"
        }
        SendMessageResult result = sqs.sendMessage(new SendMessageRequest(pushQueueUrl, gzipped))
        log.info "sent ${payloadSize} byte message=${result.messageId} data to SQS queue"
        return result
    }

    List<R> receiveSqsMessages() {
        List<R> messages = []

        // get the messages
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(pullQueueUrl)
        receiveMessageRequest.maxNumberOfMessages = maxNumberOfMessages
        log.trace "checking for messages at ${pullQueueUrl} (max ${maxNumberOfMessages})"
        sqs.receiveMessage(receiveMessageRequest).messages.each{ Message message ->
            try {
                String messageId = message.messageId
                if (messageId in received) {
                    log.debug "skipping message that has already been processed"
                } else {
                    log.info "Received Message id=${messageId} md5=${message.getMD5OfBody()}"
                    String json = StringCompressor.decompress(message.body)
                    messages << convert(json)

                    // Delete the message or mark it as ignore
                    String messageRecieptHandle = message.receiptHandle
                    if (flushCommands) {
                        sqs.deleteMessage(new DeleteMessageRequest(pullQueueUrl, messageRecieptHandle))
                    } else {
                        log.warn "Not deleting message=${messageRecieptHandle}"
                        received << messageId
                    }
                }
            } catch (Exception ex) {
                log.error('Got exception: {}', ex)
            }
        }
        return messages

    }

    // return objectMapper.readValue(json, R)
    abstract R convert(String data)

}
