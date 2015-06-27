package com.mnclimbingcoop

import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse

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

    @Inject
    HidService(DoorConfiguration config) {
        this.config = config
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

    VertXResponse get(String name, VertXRequest xml) {
        return apis[name].get(xml)
    }

}
