package com.mnclimbingcoop.web

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.domain.AccessHolder
import com.mnclimbingcoop.service.AccessHolderService

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.validation.Valid

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/access')
@Slf4j
class AccessHolderController {

    protected final AccessHolderService accessHolderService

    @Inject
    AccessHolderController(AccessHolderService accessHolderService) {
        this.accessHolderService = accessHolderService
    }

    /** Configure access for Access Holder */
    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    List<SendMessageResult> setAccess(@RequestBody @Valid AccessHolder accessHolder) {
        accessHolderService.setAccess(accessHolder)
    }

    /** Configure access for Access Holder for one door */
    @RequestMapping(value = '/{door}', method = RequestMethod.POST, produces = 'application/json')
    SendMessageResult setAccess(@PathVariable String door, @RequestBody @Valid AccessHolder accessHolder) {
        accessHolderService.setAccess(door, accessHolder)

    }

}

