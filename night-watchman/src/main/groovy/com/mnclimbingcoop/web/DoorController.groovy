package com.mnclimbingcoop.web

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.service.DoorService
import com.mnclimbingcoop.service.HidService

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/doors')
@Slf4j
class DoorController {

    protected final DoorService doorService
    protected final HidService hidService

    @Inject
    DoorController(DoorService doorService, HidService hidService) {
        this.doorService = doorService
        this.hidService = hidService
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'application/json')
    Map<String, Door> getDoors() {
        return hidService.hidStates.collectEntries{ String name, EdgeSoloState state ->
            [ name, state.doors[0] ]
        }
    }

    @RequestMapping(value = '/{door}', method = RequestMethod.GET, produces = 'application/json')
    Door getDoor(@PathVariable String door) {
        return hidService.hidStates.get(door).doors[0]
    }

    /** Locks all doors */
    @RequestMapping(value = '/lock', method = RequestMethod.POST, produces = 'application/json')
    List<SendMessageResult> lock() {
        log.info "Sending lock message to ALL doors."
        return doorService.lock()
    }

    /** Locks all doors */
    @RequestMapping(value = '/lock/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult lock(@PathVariable String door) {
        log.info "Sending lock message to '${door}'."
        return doorService.lock(door)
    }

    /** Unlocks all doors */
    @RequestMapping(value = '/unlock/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult unlock(@PathVariable String door) {
        log.info "Sending unlock message to '${door}'."
        return doorService.unlock(door)
    }

    /** Grants access to a door */
    @RequestMapping(value = '/open/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult grantAccess(@PathVariable String door) {
        log.info "Granting access to '${door}'."
        return doorService.grantAccess(door)
    }

    /** Stop alarms on a given door */
    @RequestMapping(value = '/stop-alarm/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult stopAlarm(@PathVariable String door) {
        log.info "Stop alarm on '${door}'."
        return doorService.stopAlarm(door)
    }

}

