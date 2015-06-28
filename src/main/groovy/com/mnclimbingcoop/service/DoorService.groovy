package com.mnclimbingcoop.service

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
        VertXRequest request = new DoorRequest().status()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            return [name, resp.doors?.door ]
        }
    }

    Door getDoor(String name) {
        VertXRequest request = new DoorRequest().status()
        return hidService.get(name, request)?.doors?.door
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
