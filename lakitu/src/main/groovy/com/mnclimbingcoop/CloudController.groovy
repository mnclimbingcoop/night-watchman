package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.service.DoorStateService

import javax.inject.Inject

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

    @RequestMapping(value = '/cardholders', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Cardholder>> getCardholders() {
        return doorStateService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.cardholders ]
        }
    }

    /** will seed cardholders with a predefined list */
    @RequestMapping(value = '/cardholders', method = RequestMethod.POST, produces = 'application/json')
    Integer createCardholders(Map<String, Set<Cardholder>> allCardholders) {
        allCardholders.each{ String name, Set<Cardholder> cardholders ->
            EdgeSoloState state = doorStateService.hidStates[name]
            if (state) {
                cardholders.each{ Cardholder cardholder ->
                    state.cardholders << cardholder
                }
            }
        }
    }

    @RequestMapping(value = '/state', method = RequestMethod.GET, produces = 'application/json')
    Map<String, EdgeSoloState> getState() {
        return doorStateService.hidStates
    }

}

