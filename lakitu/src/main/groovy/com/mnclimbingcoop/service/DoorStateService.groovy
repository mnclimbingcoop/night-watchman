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

    @Inject
    DoorStateService(CloudSyncService cloudSyncService) {
        this.cloudSyncService = cloudSyncService
    }

    void buildState() {
        List<EdgeSoloState> stateUpdates = cloudSyncService.receiveSqsMessages()
        if (!stateUpdates) { log.debug "nothing on the queue..." }
        stateUpdates.each { EdgeSoloState state ->
            EdgeSoloState doorState = getOrCreate(state.doorName)
            log.debug('received state: {}', objectMapper.writeValueAsString(doorState))
            doorState.merge(state)
        }
    }

    EdgeSoloState getOrCreate(String doorName) {
        EdgeSoloState doorState = hidStates[doorName]
        if (!doorState) {
            doorState = new EdgeSoloState(doorName: doorName)
            hidStates[doorName] = doorState
        }
        return doorState
    }

    List<String> getDoorNames() {
        hidStates.collect{ String name, EdgeSoloState state -> name }
    }

}
