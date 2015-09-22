package com.mnclimbingcoop.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class EventService {

    protected final HealthService healthService
    protected final HidService hidService
    protected final Map<String, EventWatcher> watchers = new ConcurrentHashMap<String, EventWatcher>()

    @Inject
    EventService(HealthService healthService, HidService hidService) {
        this.healthService = healthService
        this.hidService = hidService
    }

    @PostConstruct
    void setup() {
        hidService.doors.each{ String door ->
            log.info "Adding event watcher for ${door} door."
            watchers[door] = new EventWatcher(door, healthService, hidService)
        }
    }

    void watch() {
        watchers.each{ String door, EventWatcher watcher -> watcher.watch() }
    }

}

