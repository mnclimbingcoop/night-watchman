package com.mnclimbingcoop.async

import com.mnclimbingcoop.service.DoorStateService
import com.mnclimbingcoop.service.HeartBeatService

import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Slf4j
@Named
class InitializationService {

    protected final DoorStateService doorStateService
    protected final HeartBeatService heartBeatService

    @Inject
    InitializationService(DoorStateService doorStateService, HeartBeatService heartBeatService) {
        this.doorStateService = doorStateService
        this.heartBeatService = heartBeatService
    }

    @PostConstruct
    void startup() {
        //doorStateService.buildState()
        heartBeatService.takePulse()
    }

}
