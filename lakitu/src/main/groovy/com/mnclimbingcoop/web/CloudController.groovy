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

    @RequestMapping(value = '/cardholders', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Cardholder>> getCardholders() {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.cardholders ]
        }
    }

    @RequestMapping(value = '/cardholders/{door}', method = RequestMethod.GET, produces = 'application/json')
    Set<Cardholder> getCardholders(@PathVariable String door) {
        return doorStateService.hidStates[door].cardholders
    }

    @RequestMapping(value = '/doors', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Door>> getDoors() {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.doors ]
        }
    }

    @RequestMapping(value = '/doors/{door}', method = RequestMethod.GET, produces = 'application/json')
    Door getDoor(@PathVariable String door) {
        return doorStateService.hidStates[door].doors[0]
    }

    @RequestMapping(value = '/state', method = RequestMethod.GET, produces = 'application/json')
    Map<String, EdgeSoloState> getState() {
        return doorStateService.hidStates
    }

    @RequestMapping(value = '/state/{door}', method = RequestMethod.GET, produces = 'application/json')
    EdgeSoloState getState(@PathVariable String door) {
        return doorStateService.hidStates[door]
    }

}

