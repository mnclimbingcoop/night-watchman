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

    protected final CloudSyncService cloudSyncService
    protected final DoorConfiguration config
    protected final HealthService healthService
    protected final Map<String, HidEdgeProApi> apis = new ConcurrentHashMap<String, HidEdgeProApi>()
    protected final XmlMapper objectMapper

    // Stores the state of all HID edge units
    Map<String, EdgeSoloState> hidStates = new ConcurrentHashMap<String, EdgeSoloState>()

    @Inject
    HidService(CloudSyncService cloudSyncService,
               DoorConfiguration config,
               HealthService healthService,
               XmlMapper objectMapper) {

        this.cloudSyncService = cloudSyncService
        this.config = config
        this.healthService = healthService
        this.objectMapper = objectMapper
    }

    @PostConstruct
    protected void setup() {
        log.info "Initializing doors"
        config.devices.each{ String name, DoorConfiguration.Device device ->
            if (device.enabled) {
                log.info "*****************************************************************************************"
                log.info "Initializing HID EdgePro API for ${name} with endpoint ${device.url}"
                log.info "*****************************************************************************************"
                String username = device.username ?: config.username
                String password = device.password ?: config.password
                apis[name] = new ClientBuilder().withEndpoint(device.url)
                                                .withAuthentication(username, password)
                                                .build(HidEdgeProApi)
                hidStates[name] = new EdgeSoloState(doorName: name)
                healthService.initDoor(name, device.url)
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
        VertXResponse response = get(name, wrap(request))
        if (response?.error) {
            String xml = wrap(request)
            log.error "Error requesting XML: ${xml}"
            log.error "Error details: ${response.error}"
            healthService.getFailed(name, request, response.error.toString())
        } else {
            healthService.getSucceded(name, request)
        }
        return response
    }

    protected VertXResponse get(String name, String xml) {
        return apis[name].get(xml)
    }

    protected String wrap(VertXRequest request) {
        objectMapper.writeValueAsString(request)
    }

}
