package com.mnclimbingcoop.config

import groovy.transform.CompileStatic

import javax.inject.Named

import org.springframework.boot.context.properties.ConfigurationProperties

/** Class representation of the YML file */
@CompileStatic
@ConfigurationProperties(prefix='hidEdgePro')
@Named
public class DoorConfiguration {

    String url
    String username
    String password

    Map<String, Device> devices

    static class Device {
        String description
        String url
        String username
        String password
    }
}
