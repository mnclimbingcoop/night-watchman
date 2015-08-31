package com.mnclimbingcoop.async

import com.mnclimbingcoop.service.CommandRelayService
import com.mnclimbingcoop.service.EventService

import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Slf4j
@Named
class InitializationService {

    protected final CommandRelayService commandRelayService
    protected final EventService eventService

    @Inject
    InitializationService(CommandRelayService commandRelayService, EventService eventService) {
        this.commandRelayService = commandRelayService
        this.eventService = eventService
    }

    @PostConstruct
    void startup() {
        commandRelayService.processCommands()
        eventService.watch()
    }

}
