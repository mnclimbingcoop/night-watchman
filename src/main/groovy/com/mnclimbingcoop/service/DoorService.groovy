package com.mnclimbingcoop

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

    void recentDoorEvents() {
    }

    void validateDoorState() {
    }

}
