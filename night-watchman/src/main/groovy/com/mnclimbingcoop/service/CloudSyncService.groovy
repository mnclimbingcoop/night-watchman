package com.mnclimbingcoop.service

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.DeleteMessageRequest
import com.amazonaws.services.sqs.model.DeleteQueueRequest
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper

import java.util.zip.GZIPOutputStream
import java.util.zip.GZIPInputStream

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct

import org.joda.time.LocalTime

@CompileStatic
@Slf4j
class CloudSyncService {

    protected final HidService hidService
    protected final ObjectMapper objectMapper
    protected final AwsConfiguration awsConfig
    protected AmazonSQS sqs
    protected String queueUrl

    static final Long MAX_DATA_BYTES = 1024 * 256

    CloudSyncService(HidService hidService,
                     ObjectMapper objectMapper,
                     AwsConfiguration awsConfig) {
        this.hidService = hidService
        this.objectMapper = objectMapper
        this.awsConfig = awsConfig
    }

    String compress(String input) {
        ByteArrayOutputStream targetStream = new ByteArrayOutputStream()
        GZIPOutputStream zipStream = new GZIPOutputStream(targetStream)
        zipStream.write(input.bytes)
        zipStream.close()
        byte[] zipped = targetStream.toByteArray()
        targetStream.close()
        return zipped.encodeBase64()
    }

    @PostConstruct
    void setup() {
        AWSCredentialsProvider credProvider = new ProfileCredentialsProvider()
        AWSCredentials creds = credProvider.credentials
        sqs = new AmazonSQSClient(creds)
        sqs.region = Region.getRegion(Regions.fromName(awsConfig.region))

        CreateQueueRequest createQueueRequest = new CreateQueueRequest(awsConfig.sqs.queue)
        queueUrl = sqs.createQueue(createQueueRequest).queueUrl
        log.info "Using SQS queue: ${queueUrl}"
    }

    void sendSqsMessage() {

        // Send the message
        HappyHour hh = new HappyHour(name: 'Vino & Vinyl', time: 'Thursday', place: "Grumpy's NE")
        String payload = objectMapper.writeValueAsString(hh)

        sqs.sendMessage(new SendMessageRequest(queueUrl, payload))

    }

    void receiveSqsMessage() {

        // get the message
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        sqs.receiveMessage(receiveMessageRequest).messages.each{ Message message ->
            println "Received Message id=${message.messageId} md5=${message.getMD5OfBody()}"

            HappyHour happyHour = objectMapper.readValue(message.body, HappyHour)
            println "HappyHour: ${happyHour.name}, ${happyHour.time}, ${happyHour.place}"

            // Delete the message
            String messageRecieptHandle = message.receiptHandle
            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle))

        }

    }

    protected String decompress(String input) {
        byte[] zipped = = input.decodeBase64()

        ByteArrayOutputStream targetStream = new ByteArrayOutputStream()
        GZIPOutputStream zipStream = new GZIPOutputStream(targetStream)
        zipStream.write(input.bytes)
        zipStream.close()
        byte[] zipped = targetStream.toByteArray()
        targetStream.close()
        return zipped.encodeBase64()
    }

    protected String compress(String input) {
        ByteArrayOutputStream targetStream = new ByteArrayOutputStream()
        GZIPOutputStream zipStream = new GZIPOutputStream(targetStream)
        zipStream.write(input.bytes)
        zipStream.close()
        byte[] zipped = targetStream.toByteArray()
        targetStream.close()
        return zipped.encodeBase64()
    }

}
