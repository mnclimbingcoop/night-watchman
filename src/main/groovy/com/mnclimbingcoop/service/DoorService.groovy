package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.DoorRequest

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

    Map<String, Door> getDoors() {
        Map<String, Door> doors = [:]

        hidService.doors.each{ String name ->

            VertXRequest xml = new DoorRequest().status()
            String request = requestBuilder.wrap(xml)

            VertXResponse response = hidService.get(name, request)

            Door door = response.doors?.door
            if (door) {
                doors.name = door
            } else {
                log.error "No door named '${name}' found!"
            }
        }
        return doors
    }


}
