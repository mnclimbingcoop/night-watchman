package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration

import javax.annotation.PostConstruct
import javax.inject.Inject

class DoorService {

    final UrlRequestBuilder requestBuilder
    final XmlMapper objectMapper
    final DoorConfiguration config
    Map<String, HidEdgeProApi> doorClients = [:]

    @PostConstruct
    void setup() {

        log.debug "Initializing doors"
        config.devices.each{ device ->
            log.info "Initializing HID EdgePro API fro ${device.name} with endpoint ${device.url}"
            String username = device.username ?: config.username
            String password = device.password ?: config.password
            apis[device.name] = new ClientBuilder().withEndpoint(device.url)
                                                   .withAuthentication(username, password)
                                                   .build(HidEdgeProApi)
        }

    }


    @Inject
    DoorService(XmlMapper objectMapper, DoorConfiguration config) {
        this.requestBuilder = new UrlRequestBuilder()
        this.objectMapper = objectMapper
        this.config = config
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
