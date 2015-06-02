package com.mnclimbingcoop.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder
import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@CompileStatic
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = [ 'com.mnclimbingcoop' ])
@EnableScheduling
@Slf4j
class DoorWatchmanApp {

    static void main(final String[] args) {
        SpringApplication.run(this, args)
    }

    @Value('${hidEdgePro.uri}')
    String hidEdgeProUrl


    @Bean
    HidEdgeProApi hidEdgeProApi() {
        log.info "Initilizing HID EdgePro API with endpoint ${hidEdgeProUrl}"
        return new ClientBuilder().withEndpoint(hidEdgeProUrl).build(HidEdgeProApi)
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapperBuilder().build()
    }

}
