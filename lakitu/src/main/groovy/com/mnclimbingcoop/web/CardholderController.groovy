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

@RequestMapping('/cardholders')
@RestController
class CardholderController {

    protected final DoorStateService doorStateService

    @Inject
    CardholderController(DoorStateService doorStateService) {
        this.doorStateService = doorStateService
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Cardholder>> getCardholders() {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.cardholders ]
        }
    }

    @RequestMapping(value = '/{door}', method = RequestMethod.GET, produces = 'application/json')
    Set<Cardholder> getCardholders(@PathVariable String door) {
        return doorStateService.hidStates[door].cardholders
    }

    @RequestMapping(value = '/find/{door}/{q}', method = RequestMethod.GET, produces = 'application/json')
    Set<Cardholder> findCardholders(@PathVariable String door, @PathVariable String q) {
        return doorStateService.hidStates[door].findCardholders(q)
    }

    @RequestMapping(value = '/find/{q}', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Cardholder>> findCardholders(@PathVariable String q) {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.findCardholders(q) ]
        }
    }

}

