package com.mnclimbingcoop.web

import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.service.DoorStateService

import javax.inject.Inject

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping('/events')
@RestController
class EventController {

    protected final DoorStateService doorStateService

    @Inject
    EventController(DoorStateService doorStateService) {
        this.doorStateService = doorStateService
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<EventMessage>> getEvents() {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.events ]
        }
    }

    @RequestMapping(value = '/{door}', method = RequestMethod.GET, produces = 'application/json')
    Set<EventMessage> getEvents(@PathVariable String door) {
        return doorStateService.hidStates[door].events
    }

}

