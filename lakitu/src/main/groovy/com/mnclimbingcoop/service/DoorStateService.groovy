package com.mnclimbingcoop.service

import org.springframework.scheduling.annotation.Async

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder
import com.mnclimbingcoop.domain.EdgeSoloState

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class DoorStateService {

    Map<String, EdgeSoloState> hidStates = new ConcurrentHashMap<String, EdgeSoloState>()
    protected final CloudSyncService cloudSyncService
    protected final ObjectMapper objectMapper = new ObjectMapperBuilder().build()
    protected final DoorMerger doorMerger = new DoorMerger()
    protected final HealthService healthService

    @Inject
    DoorStateService(CloudSyncService cloudSyncService, HealthService healthService) {
        this.cloudSyncService = cloudSyncService
        this.healthService = healthService
    }

    @Async
    void buildState() {
        log.info "preparing to take pulse."
        // Wait 2s before starting
        Thread.sleep(2000)
        log.info "taking pulse."
        cloudSyncService.observable.subscribe(
            { EdgeSoloState state ->
                if (state.doorName) {
                    EdgeSoloState doorState = getOrCreate(state.doorName)
                    log.trace('received state: {}', objectMapper.writeValueAsString(state))
                    doorMerger.merge(doorState, state)
                } else {
                    log.error('door name missing from incoming state data! {}', objectMapper.writeValueAsString(state))
                }
            }, { Throwable t ->
                log.error "Error while taking pulse ${t.class} ${t.message}"
                healthService.checkMessagesFailed()
            }
        )
    }

    EdgeSoloState getOrCreate(String doorName) {
        if (hidStates.containsKey(doorName)) { return hidStates[doorName] }
        log.info "Found new door '${doorName}'"

        EdgeSoloState doorState = new EdgeSoloState(doorName: doorName)
        hidStates[doorName] = doorState
        return doorState
    }

    List<String> getDoorNames() {
        hidStates.collect{ String name, EdgeSoloState state -> name }
    }

}
