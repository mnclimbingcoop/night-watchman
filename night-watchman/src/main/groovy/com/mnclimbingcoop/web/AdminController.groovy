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

    @RequestMapping(value = '/credentials/{door}', method = RequestMethod.GET, produces = 'application/json')
    Set<Credential> getCredentials(@PathVariable String door) {
        return hidService.hidStates[door].credentials

    }

    @RequestMapping(value = '/credentials', method = RequestMethod.GET, produces = 'application/json')
    Map<String, Set<Credential>> getCredentials() {
        return hidService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.credentials ]
        }
    }

    @RequestMapping(value = '/state', method = RequestMethod.GET, produces = 'application/json')
    Map<String, EdgeSoloState> getState() {
        return hidService.hidStates
    }

    // TODO: Add events

    @RequestMapping(value = '/state/{door}', method = RequestMethod.GET, produces = 'application/json')
    EdgeSoloState getState(@PathVariable door) {
        return hidService.hidStates.get(door)
    }


}

