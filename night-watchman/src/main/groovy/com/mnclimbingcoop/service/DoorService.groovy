package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.ControlRequest
import com.mnclimbingcoop.request.DoorRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class DoorService {

    // TODO:  Support "Unlock Until" command states that ensure door is unlocked

    protected final HidService hidService
    protected final HealthService healthService

    @Inject
    DoorService(HealthService healthService, HidService hidService) {
        this.hidService = hidService
        this.healthService = healthService
    }

    Map<String, Doors> list() {
        VertXRequest request = new DoorRequest().list()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.doors) {
                updateState(name, resp.doors)
            }
            return [ name, resp.doors ]
        }
    }

    Doors list(String name) {
        VertXRequest request = new DoorRequest().list()
        Doors doors = hidService.get(name, request)?.doors
        if (doors) {
            updateState(name, doors)
        }
        return doors
    }

    void updateState(String name, Doors doors) {
        healthService.checkedDoor(name)
        // Only 1 door to support
        List<Door> currentDoors = doors.doors
        EdgeSoloState state = hidService.hidStates[name]
        Set<Door> lastDoors = state.doors
        boolean detectedChange = currentDoors.any{ Door currentDoor ->
            lastDoors.any{ Door lastDoor ->
                if (currentDoor.changed(lastDoor)) {
                    log.info "Door state (${currentDoor.relayState}) for [${name}] " +
                             "changed from (${lastDoor.relayState}), sending notification."
                    return true
                }
            }
        }

        // Update state with new doors
        hidService.hidStates[name].doors.clear()
        currentDoors.each{ Door door ->
            hidService.hidStates[name].doors.add(door)
        }

        if (detectedChange) {
            currentDoors.each{ Door door -> sync(name, door) }
            healthService.updatedDoor(name)
        }
    }

    void sync(String name, Door door) {
        EdgeSoloState state = new EdgeSoloState(doorName: name)
        state.doors << door
        hidService.sync(state)
    }

    void unlock(String door) {
        VertXRequest request = new DoorRequest().unlock()
        updateDoor(door, request)
    }

    void lock() {
        hidService.doors.each{ String door ->
            VertXRequest request = new DoorRequest().lock()
            updateDoor(door, request)
        }
    }

    void lock(String door) {
        VertXRequest request = new DoorRequest().lock()
        updateDoor(door, request)
    }

    void stopAlarm(String door) {
        VertXRequest request = new ControlRequest().stopAlarm()
        updateDoor(door, request)
    }

    void grantAccess(String door) {
        VertXRequest request = new DoorRequest().grantAccess()
        updateDoor(door, request)
    }

    protected Door updateDoor(String door, VertXRequest request) {
        List<Door> result = hidService.get(door, request).doors?.doors
        if (result) { return result[0] }
        return null
    }

}
