package com.mnclimbingcoop

import com.mnclimbingcoop.service.*

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class StateSlurper {

    protected final DoorStateService doorStateService

    @Inject
    StateSlurper(DoorStateService doorStateService) {
        this.doorStateService = doorStateService
    }

    /** Check the queue every 20 seconds */
    @Scheduled(cron = '*/20 * * * * *')
    void secure() {
        doorStateService.buildState()
    }

}
