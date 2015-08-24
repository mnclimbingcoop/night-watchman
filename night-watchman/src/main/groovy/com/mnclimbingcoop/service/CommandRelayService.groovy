package com.mnclimbingcoop.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder
import com.mnclimbingcoop.domain.VertXRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CommandRelayService {

    protected final CloudSyncService cloudSyncService
    protected final HidService hidService
    protected final ObjectMapper objectMapper = new ObjectMapperBuilder().build()

    protected final OrchestratorService orchestrator

    @Inject
    CommandRelayService(CloudSyncService cloudSyncService, HidService hidService, OrchestratorService orchestrator) {
        this.cloudSyncService = cloudSyncService
        this.hidService = hidService
        this.orchestrator = orchestrator
    }

    void processCommands() {
        List<VertXRequest> requests = cloudSyncService.receiveSqsMessages()
        requests.each { VertXRequest request ->
            if (request.meta) {
                orchestrator.orchestrate(request)
                log.debug('processed meta command: {}', objectMapper.writeValueAsString(request.meta))
            } else {
                hidService.get(request)
                log.debug('processed command: {}', objectMapper.writeValueAsString(request))
            }
        }
    }

}
