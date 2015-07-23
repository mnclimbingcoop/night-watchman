package com.mnclimbingcoop.service

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

    @Inject
    DoorStateService(CloudSyncService cloudSyncService) {
        this.cloudSyncService = cloudSyncService
    }

    void buildState() {
        List<EdgeSoloState> stateUpdates = cloudSyncService.receiveSqsMessages()
        stateUpdates.each { EdgeSoloState state ->
            EdgeSoloState doorState = getOrCreate(state.doorName)
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
