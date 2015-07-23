package com.mnclimbingcoop.web

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.service.DoorService

import groovy.transform.CompileStatic

import javax.inject.Inject

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/doors')
class DoorController {

    protected final DoorService doorService

    @Inject
    DoorController(DoorService doorService) {
        this.doorService = doorService
    }

    /** Locks all doors */
    @RequestMapping(value = '/lock', method = RequestMethod.POST, produces = 'application/json')
    List<SendMessageResult> lock() {
        return doorService.lock()
    }

    /** Locks all doors */
    @RequestMapping(value = '/lock/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult lock(@PathVariable String door) {
        return doorService.lock(door)
    }

    /** Unlocks all doors */
    @RequestMapping(value = '/unlock', method = RequestMethod.POST, produces = 'application/json')
    List<SendMessageResult> unlock() {
        return doorService.unlock()
    }

    /** Unlocks all doors */
    @RequestMapping(value = '/unlock/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult unlock(@PathVariable String door) {
        return doorService.unlock(door)
    }

    /** Grants access to a door */
    @RequestMapping(value = '/open/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult grantAccess(@PathVariable String door) {
        return doorService.grantAccess(door)
    }


}

