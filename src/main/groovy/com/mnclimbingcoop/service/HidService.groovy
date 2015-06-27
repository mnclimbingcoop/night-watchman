package com.mnclimbingcoop

import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse

import com.fasterxml.jackson.dataformat.xml.XmlMapper

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class HidService {

    protected final DoorConfiguration config
    protected final Map<String, HidEdgeProApi> apis = [:]
    protected final XmlMapper objectMapper

    @Inject
    HidService(DoorConfiguration config, XmlMapper objectMapper) {
        this.config = config
        this.objectMapper = objectMapper
    }

    @PostConstruct
    protected void setup() {
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

    Set<String> getDoors() {
        apis.keySet()
    }

    VertXResponse get(String name, String xml) {
        return apis[name].get(xml)
    }

    VertXResponse get(String name, VertXRequest request) {
        return apis[name].get(wrap(request))
    }

    protected String wrap(VertXRequest request) {
        objectMapper.writeValueAsString(request)
    }

}