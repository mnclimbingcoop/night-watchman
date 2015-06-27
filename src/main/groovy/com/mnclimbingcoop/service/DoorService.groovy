package com.mnclimbingcoop

import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.VertXResponse

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class DoorService {

    protected final HidService hidService
    protected final UrlRequestBuilder requestBuilder

    @Inject
    DoorService(HidService hidService, UrlRequestBuilder requestBuilder) {
        this.hidService = hidService
        this.requestBuilder = requestBuilder
    }

    void getDoorStatus() {
        hidService.doors.each{ String name ->
            HidEdgeProApi api = hidService.getApi(name)
            String request = requestBuilder.doorStatus()
            VertXResponse response = api.get(request)
            Door door = response.doors?.door
            if (door) {
                log.info "Door [${name}]: ${door.doorName} - ${door.relayState}"
            } else {
                log.error "NO DOOR! ${name}"
            }
        }
    }

    void recentDoorEvents() {
    }

    void validateDoorState() {
    }

}
