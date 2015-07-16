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
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.VertXRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CloudSyncService {

    protected final ObjectMapper objectMapper
    protected final AwsConfiguration awsConfig
    protected AmazonSQS sqs
    protected String queueUrl

    static final Long MAX_DATA_BYTES = 1024 * 256

    @Inject
    CloudSyncService(ObjectMapper objectMapper,
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

        createQueue()
    }

    protected void createQueue() {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(awsConfig.sqs.queue)
        queueUrl = sqs.createQueue(createQueueRequest).queueUrl
        log.info "Using SQS queue: ${queueUrl}"
    }

    void sendSqsMessage(EdgeSoloState state) {
        String payload = objectMapper.writeValueAsString(state)
        String gzipped = StringCompressor.compress(payload)
        SendMessageResult result = sqs.sendMessage(new SendMessageRequest(queueUrl, gzipped))
        log.info "sent message=${result.messageId} state data to SQS queue"
    }

    List<VertXRequest> receiveSqsMessages() {
        List<VertXRequest> requests = []

        // get the message
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        sqs.receiveMessage(receiveMessageRequest).messages.each{ Message message ->

            println "Received Message id=${message.messageId} md5=${message.getMD5OfBody()}"
            String json = StringCompressor.decompress(message.body)
            requests << objectMapper.readValue(json, VertXRequest)

            // Delete the message
            String messageRecieptHandle = message.receiptHandle
            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle))

        }
        return requests

    }

}
