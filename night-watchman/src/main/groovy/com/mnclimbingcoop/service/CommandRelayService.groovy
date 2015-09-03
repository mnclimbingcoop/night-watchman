package com.mnclimbingcoop.service

import org.springframework.scheduling.annotation.Async

import com.fasterxml.jackson.databind.ObjectMapper

import com.mnclimbingcoop.ObjectMapperBuilder
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse

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
    protected final HealthService healthService
    protected final OrchestratorService orchestrator

    @Inject
    CommandRelayService(CloudSyncService cloudSyncService,
                        HealthService healthService,
                        HidService hidService,
                        OrchestratorService orchestrator) {
        this.cloudSyncService = cloudSyncService
        this.healthService = healthService
        this.hidService = hidService
        this.orchestrator = orchestrator
    }

    @Async
    void processCommands() {
        log.info "preparing to process commands."
        // Wait 10s before starting
        Thread.sleep(10000)
        log.info "processing commands."
        cloudSyncService.observable.cast(VertXRequest).subscribe(
            { VertXRequest request ->
                if (request.meta) {
                    orchestrator.orchestrate(request)
                    log.debug('processed meta command: {}', objectMapper.writeValueAsString(request.meta))
                } else {
                    hidService.get(request)
                    log.debug('processed command: {}', objectMapper.writeValueAsString(request))
                }
            }, { Throwable t ->
                log.error "Error while reading card holders ${t.class} ${t.message}"
                healthService.checkMessagesFailed()
            }
        )
    }

}
