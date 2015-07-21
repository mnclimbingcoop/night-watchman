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

    /** Check the doors every minute */
    //@Scheduled(cron = '0 * * * * *')
    void secure() {
        // This should probably be checked more often (every 20 sec?)
        doorService.list()
        // listen via SQS
    }

}
