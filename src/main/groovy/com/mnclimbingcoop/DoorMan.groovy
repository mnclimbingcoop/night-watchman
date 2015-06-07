package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.VertXMessage

import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class DoorMan {

    @Inject
    DoorConfiguration config

    @Inject
    XmlMapper xmlObjectMapper

    RequestBuilder requestBuilder = new RequestBuilder()

    Map<String, HidEdgeProApi> apis = [:]

    @PostConstruct
    void setup() {

        log.info "Initializing Doors!"
        config.devices.each{ device ->
            log.info "Initilizing HID EdgePro API fro ${device.name} with endpoint ${device.url}"
            String username = device.username ?: config.username
            String password = device.password ?: config.password
            apis[device.name] = new ClientBuilder().withEndpoint(device.url)
                                                   // .withUnsafeSSL()
                                                   .withAuthentication(username, password)
                                                   .build(HidEdgeProApi)
        }

    }

    @Scheduled(fixedRate = 5000l)
    void ping() {

        apis.each{ String name, HidEdgeProApi api ->
            log.info '>> Getting door status'
            VertXMessage response = api.get(getXml('doorStatus'))

            Door door = response.doors.door

            log.info "Door [${name}]: ${door.doorName} - ${door.relayState}"
        }

    }


    String getXml(String method) {
        xmlObjectMapper.writeValueAsString(requestBuilder."${method}"())
    }
}
