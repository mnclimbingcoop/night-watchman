package com.mnclimbingcoop.config

import groovy.transform.CompileStatic

import javax.inject.Named

import org.springframework.boot.context.properties.ConfigurationProperties

/** Class representation of the YML file */
@CompileStatic
@ConfigurationProperties(prefix='aws')
@Named
public class AwsConfiguration {

    String region
    SqsConfig sqs

    static class SqsConfig {
        Boolean enabled = true
        String queue
    }
}
