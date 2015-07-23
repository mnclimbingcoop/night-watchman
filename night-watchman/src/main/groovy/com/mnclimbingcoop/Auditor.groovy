package com.mnclimbingcoop

import com.mnclimbingcoop.service.EventService

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class Auditor {

    protected final EventService eventService

    @Inject
    Auditor(EventService eventService) {
        this.eventService = eventService
    }

    /** Check for events every 3 seconds */
    @Scheduled(fixedDelayString = '${schedule.events.rate}', initialDelayString = '${schedule.events.initialDelay}')
    void monitor() {
        eventService.poll()
    }

}
