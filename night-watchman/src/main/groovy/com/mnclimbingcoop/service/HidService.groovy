package com.mnclimbingcoop.service

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class HidService {

    protected final DoorConfiguration config
    protected final Map<String, HidEdgeProApi> apis = new ConcurrentHashMap<String, HidEdgeProApi>()
    protected final XmlMapper objectMapper
    protected final CloudSyncService cloudSyncService

    // Stores the state of all HID edge units
    Map<String, EdgeSoloState> hidStates = new ConcurrentHashMap<String, EdgeSoloState>()

    @Inject
    HidService(DoorConfiguration config,
               CloudSyncService cloudSyncService,
               XmlMapper objectMapper) {

        this.cloudSyncService = cloudSyncService
        this.config = config
        this.objectMapper = objectMapper
    }

    @PostConstruct
    protected void setup() {
        log.info "Initializing doors"
        config.devices.each{ String name, DoorConfiguration.Device device ->
            if (device.enabled) {
                log.info "Initializing HID EdgePro API for ${name} with endpoint ${device.url}"
                String username = device.username ?: config.username
                String password = device.password ?: config.password
                apis[name] = new ClientBuilder().withEndpoint(device.url)
                                                .withAuthentication(username, password)
                                                .build(HidEdgeProApi)
                hidStates[name] = new EdgeSoloState()
            }
        }

    }

    void sync() {
        hidStates.each{ String name, EdgeSoloState state -> sync(state) }
    }

    void sync(EdgeSoloState state) {
        cloudSyncService.sendSqsMessage(state)
    }

    void pull() {
        List<VertXRequest> requests = cloudSyncService.receiveSqsMessages()
        requests.each{ VertXRequest request ->
            getAll(request)
        }
    }


    Set<String> getDoors() {
        apis.keySet()
    }

    Map<String, Object> getAll(VertXRequest request, Closure closure) {
        return getAll(request).collectEntries(closure)
    }

    Map<String, VertXResponse> getAll(VertXRequest request) {
        Map<String, VertXResponse> responses = [:]
        doors.each{ String name ->
            responses[name] = get(name, request)
        }
        return responses
    }

    VertXResponse get(VertXRequest request) {
        return get(request.doorName, request)
    }

    VertXResponse get(String name, VertXRequest request) {
        return get(name, wrap(request))
    }

    VertXResponse get(String name, String xml) {
        return apis[name].get(xml)
    }

    protected String wrap(VertXRequest request) {
        objectMapper.writeValueAsString(request)
    }

}
