package com.mnclimbingcoop.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class EventService {

    protected final HealthService healthService
    protected final HidService hidService
    protected final List<EventWatcher> watchers = []

    @Inject
    EventService(HealthService healthService, HidService hidService) {
        this.healthService = healthService
        this.hidService = hidService

    }

    @PostConstruct
    void setup() {
        hidService.doors.each{ String door ->
            log.info "Adding event watcher for ${door} door."
            watchers << new EventWatcher(door, healthService, hidService)
        }
    }

    void watch() {
        watchers.each{ EventWatcher watcher -> watcher.watch() }
    }

}

