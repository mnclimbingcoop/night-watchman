package com.mnclimbingcoop.web

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.service.DoorStateService

import javax.inject.Inject

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class CloudController {

    protected final DoorStateService doorStateService

    @Inject
    CloudController(DoorStateService doorStateService) {
        this.doorStateService = doorStateService
    }

    @RequestMapping(value = '/credentials', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Credential>> getCredentials() {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.credentials ]
        }
    }

    @RequestMapping(value = '/credentials/{door}', method = RequestMethod.GET, produces = 'application/json')
    Set<Credential> getCredentials(@PathVariable String door) {
        return doorStateService.hidStates[door].credentials
    }

    // TODO: Add events

    @RequestMapping(value = '/state', method = RequestMethod.GET, produces = 'application/json')
    Map<String, EdgeSoloState> getState() {
        return doorStateService.hidStates
    }

    @RequestMapping(value = '/state/{door}', method = RequestMethod.GET, produces = 'application/json')
    EdgeSoloState getState(@PathVariable String door) {
        return doorStateService.hidStates[door]
    }

}

