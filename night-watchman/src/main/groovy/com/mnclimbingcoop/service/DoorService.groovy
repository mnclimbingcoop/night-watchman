package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.Doors
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

    Map<String, Doors> list() {
        VertXRequest request = new DoorRequest().list()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.doors) {
                hidService.hidStates[name].doors.addAll(resp.doors.doors)
            }
            return [ name, resp.doors ]
        }
    }

    Doors list(String name) {
        VertXRequest request = new DoorRequest().list()
        Doors doors = hidService.get(name, request)?.doors
        if (doors) {
            hidService.hidStates[name].doors.addAll(doors.doors)
        }
        return doors
    }

    Door getDoor(String name) {
        list(name)?.door
    }

    void unlockDoor(String name) {
        VertXRequest request = new DoorRequest().unlock()
        hidService.get(name, request)
    }

    void lockDoor(String name) {
        VertXRequest request = new DoorRequest().lock()
        hidService.get(name, request)
    }

    void openDoor(String name) {
        VertXRequest request = new DoorRequest().grantAccess()
        hidService.get(name, request)
    }


}
