package com.mnclimbingcoop.web

import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.service.HidService

import javax.inject.Inject

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping('/events')
@RestController
class EventController {

    protected final HidService hidService

    @Inject
    EventController(HidService hidService) {
        this.hidService = hidService
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<EventMessage>> getEvents() {
        return hidService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.events ]
        }
    }

    @RequestMapping(value = '/{door}', method = RequestMethod.GET, produces = 'application/json')
    Set<EventMessage> getEvents(@PathVariable String door) {
        return hidService.hidStates[door].events
    }

}

