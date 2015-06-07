package com.mnclimbingcoop.config

import javax.inject.Named

import org.springframework.boot.context.properties.ConfigurationProperties

/** Class representation of the YML file */
@Named
@ConfigurationProperties(prefix="hidEdgePro")
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
