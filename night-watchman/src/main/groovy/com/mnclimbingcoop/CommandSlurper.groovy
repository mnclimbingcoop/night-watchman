package com.mnclimbingcoop

import com.mnclimbingcoop.service.CommandRelayService

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class CommandSlurper {

    protected final CommandRelayService commandRelayService

    @Inject
    CommandSlurper(CommandRelayService commandRelayService) {
        this.commandRelayService = commandRelayService
    }

    /** Check the queue every 3 seconds */
    @Scheduled(fixedDelayString = '${schedule.commands.rate}', initialDelayString = '${schedule.commands.initialDelay}')
    void relay() {
        commandRelayService.processCommands()
    }

}
