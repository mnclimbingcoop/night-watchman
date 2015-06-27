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

    @Inject
    DoorService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, Door> getDoors() {
        Map<String, Door> doors = [:]

        hidService.doors.each{ String name ->

            VertXRequest request = new DoorRequest().status()
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
