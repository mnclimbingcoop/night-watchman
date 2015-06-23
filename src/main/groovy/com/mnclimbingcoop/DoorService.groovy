package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class DoorService {

    final UrlRequestBuilder requestBuilder
    final XmlMapper objectMapper
    final DoorConfiguration config
    Map<String, HidEdgeProApi> apis = [:]

    @Inject
    DoorService(XmlMapper objectMapper, DoorConfiguration config, UrlRequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder
        this.objectMapper = objectMapper
        this.config = config
    }

    @PostConstruct
    void setup() {

        log.debug "Initializing doors"
        config.devices.each{ String name, DoorConfiguration.Device device ->
            log.info "Initializing HID EdgePro API fro ${name} with endpoint ${device.url}"
            String username = device.username ?: config.username
            String password = device.password ?: config.password
            apis[name] = new ClientBuilder().withEndpoint(device.url)
                                                   .withAuthentication(username, password)
                                                   .build(HidEdgeProApi)
        }

    }

    void recentDoorEvents() {
    }

    void validateDoorState() {
    }

    void buildUserDatabase() {
    }

    void buildCredentialDatabase() {
    }

    void buildScheduleDatabase() {
    }

}
