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

    @Scheduled(fixedDelayString = '${schedule.state.rate}', initialDelayString = '${schedule.state.initialDelay}')
    void secure() {
        doorStateService.buildState()
    }

}
