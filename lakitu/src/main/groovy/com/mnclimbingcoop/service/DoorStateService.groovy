package com.mnclimbingcoop.service

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

    @Inject
    DoorStateService(CloudSyncService cloudSyncService) {
        this.cloudSyncService = cloudSyncService
    }

    void buildState() {
        List<EdgeSoloState> stateUpdates = cloudSyncService.receiveSqsMessages()
        stateUpdates.each { EdgeSoloState state ->
            if (state.doorName) {
                EdgeSoloState doorState = getOrCreate(state.doorName)
                log.trace('received state: {}', objectMapper.writeValueAsString(state))
                doorMerger.merge(doorState, state)
            }
        }
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
