
package com.mnclimbingcoop.service

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.domain.AccessHolder

import groovy.transform.CompileStatic

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
class AccessHolderService {

    protected final OrchestratorService orchestrator
    protected final HidService hidService

    @Inject
    AccessHolderService(OrchestratorService orchestrator, HidService hidService) {
        this.orchestrator = orchestrator
        this.hidService = hidService
    }

    void setAccess(String door, AccessHolder accessHolder) {
        orchestrator.orchestrate(door, accessHolder)
    }

    void setAccess(AccessHolder accessHolder) {
        List<SendMessageResult> results = []
        hidService.doors.each{ String door ->
            orchestrator.orchestrate(door, accessHolder)
        }
    }

}
