package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.VertXMessage

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class DoorMan {

    protected final DoorConfiguration config
    protected final XmlMapper xmlMapper
    protected final UrlRequestBuilder requestBuilder

    Map<String, HidEdgeProApi> apis = [:]

    @Inject
    DoorMan(XmlMapper xmlMapper, DoorConfiguration config, UrlRequestBuilder requestBuilder) {
        this.xmlMapper = xmlMapper
        this.config = config
        this.requestBuilder = requestBuilder
    }

    @PostConstruct
    void setup() {

        log.info "Initializing Doors!"
        config.devices.each{ String name, DoorConfiguration.Device device ->
            log.info "Initilizing HID EdgePro API fro ${name} with endpoint ${device.url}"
            String username = device.username ?: config.username
            String password = device.password ?: config.password
            apis[name] = new ClientBuilder().withEndpoint(device.url)
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
        xmlMapper.writeValueAsString(requestBuilder."${method}"())
    }
}
