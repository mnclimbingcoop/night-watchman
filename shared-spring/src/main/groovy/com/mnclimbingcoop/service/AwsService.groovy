package com.mnclimbingcoop.service

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class AwsService {

    AWSCredentialsProvider credentialsProvider

    protected final String region

    AwsService(String region) {
        this.region = region
        credentialsProvider = new DefaultAWSCredentialsProviderChain()
    }

    protected AmazonSQS getSqsClient() {
        AWSCredentials credentials = credentialsProvider.credentials
        AmazonSQS client = new AmazonSQSClient(credentials)
        client.region = Region.getRegion(Regions.fromName(region))
        return client
    }

}

