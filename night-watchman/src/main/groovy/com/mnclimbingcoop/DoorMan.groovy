package com.mnclimbingcoop

import com.mnclimbingcoop.service.*

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class DoorMan {

    protected final DoorService doorService

    @Inject
    DoorMan(DoorService doorService) {
        this.doorService = doorService
    }

    @Scheduled(fixedDelayString = '${schedule.door.rate}', initialDelayString = '${schedule.door.initialDelay}')
    void secure() {
        doorService.list()
    }

}
