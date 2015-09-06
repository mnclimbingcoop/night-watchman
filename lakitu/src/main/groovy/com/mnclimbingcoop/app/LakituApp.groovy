package com.mnclimbingcoop.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.Executor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@CompileStatic
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = [
    'com.mnclimbingcoop',
    'com.mnclimbingcoop.app',
    'com.mnclimbingcoop.config',
    'com.mnclimbingcoop.service',
    'com.mnclimbingcoop.web'
])
@Slf4j
class LakituApp {

    static final ObjectMapper MAPPER = ObjectMapperBuilder.build()

    static void main(final String[] args) {
        SpringApplication.run(this, args)
    }

}
