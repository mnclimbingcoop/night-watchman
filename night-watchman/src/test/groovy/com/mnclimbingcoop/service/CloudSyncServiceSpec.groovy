package com.mnclimbingcoop.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder
import com.mnclimbingcoop.config.AwsConfiguration

import spock.lang.Specification

class CloudSyncServiceSpec extends Specification {

    CloudSyncService service
    HidService hidService
    ObjectMapper objectMapper
    AwsConfiguration awsConfig

    void setup() {

        hidService = Mock()
        objectMapper = new ObjectMapperBuilder().build()
        awsConfig = new AwsConfiguration(
            region: 'us-east-1',
            sqs: new AwsConfiguration.SqsConfig(queue: 'test')
        )

        service = new CloudSyncService(hidService, objectMapper, awsConfig)
    }

}
