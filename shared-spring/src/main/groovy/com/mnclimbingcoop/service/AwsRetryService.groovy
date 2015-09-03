package com.mnclimbingcoop.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient

import org.apache.http.NoHttpResponseException

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class AwsRetryService<R> {

    protected final AwsService awsService

    protected AmazonSQS sqs

    AwsRetryService(AwsService awsService) {
        this.awsService = awsService
        sqs = awsService.getSqsClient()
    }

    R withRetry(String description, Closure<R> cls) {
        return withRetry(description, 0, cls)
    }

    R withRetry(String description, Integer maxTries, Closure<R> cls) {
        int tried = 0
        boolean success = false
        R result = null
        while (!success && (!maxTries || tried < maxTries)) {
            try {
                result = cls(sqs)
                success = true
            } catch ( SocketTimeoutException | NoHttpResponseException | AmazonServiceException ex) {
                log.error "Error ${description}", ex
                Thread.sleep(500 * tried)
                sqs = awsService.getSqsClient()
            }
            tried++
        }
        if (tried > 1) {
            if (success) {
                log.info 'Retry / Reauthenticatiion Successful.'
            } else {
                log.error "Gave up ${description}. Max retries reached ${maxTries}"
            }
        }
        return result
    }

}

