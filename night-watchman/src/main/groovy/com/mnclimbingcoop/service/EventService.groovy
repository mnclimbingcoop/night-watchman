package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.EventRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class EventService {

    protected final HidService hidService

    @Inject
    EventService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, EventMessage> poll() {
        log.warn "TODO: Implement"
    }

    Map<String, EventMessage> getlatest() {
        VertXRequest request = new EventRequest().overview()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.eventMessages) {
                hidService.hidStates[name].eventOverview = resp.eventMessages
                hidService.hidStates[name].events.addAll(resp.eventMessages.eventMessages)
            }
            sync()
            return [ name, resp.doors ]
        }
    }


    void sync() {
        hidService.sync()
    }

}
