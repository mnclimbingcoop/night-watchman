package com.mnclimbingcoop.config

import groovy.transform.CompileStatic

import javax.inject.Named

import org.springframework.boot.context.properties.ConfigurationProperties

/** Class representation of the YML file */
@CompileStatic
@ConfigurationProperties(prefix='api')
@Named
public class ApiKeyConfiguration {

    Map<String, String> keys = [:]

}
