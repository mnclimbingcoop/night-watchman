package com.mnclimbingcoop.web

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.service.HidService

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
class AdminController {

    protected final HidService hidService

    @Inject
    AdminController(HidService hidService) {
        this.hidService = hidService
    }

    @RequestMapping(value = '/credentials', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Credential>> getCredentials() {
        return hidService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.credentials ]
        }
    }

    @RequestMapping(value = '/cardholders', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Cardholder>> getCardholders() {
        return hidService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.cardholders ]
        }
    }

    @RequestMapping(value = '/state', method = RequestMethod.GET, produces = 'application/json')
    Map<String, EdgeSoloState> getState() {
        return hidService.hidStates
    }

    @RequestMapping(value = '/state/{door}', method = RequestMethod.GET, produces = 'application/json')
    EdgeSoloState getState(@PathVariable door) {
        log.info "getting state for '${door}'"
        return hidService.hidStates.get(door)
    }

    /** will seed cardholders with a predefined list */
    @RequestMapping(value = '/cardholders', method = RequestMethod.POST, produces = 'application/json')
    Integer createCardholders(@RequestBody Map<String, Set<Cardholder>> allCardholders) {
        Integer created = 0
        allCardholders.each{ String name, Set<Cardholder> cardholders ->
            EdgeSoloState state = hidService.hidStates[name]
            if (state) {
                cardholders.each{ Cardholder cardholder ->
                    state.cardholders << cardholder
                    created++
                }
            }
        }
        return created
    }


}

