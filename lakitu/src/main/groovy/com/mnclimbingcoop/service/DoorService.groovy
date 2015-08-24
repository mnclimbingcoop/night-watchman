package com.mnclimbingcoop.service

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.request.ControlRequest
import com.mnclimbingcoop.request.DoorRequest

import groovy.transform.CompileStatic

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
class DoorService {

    protected final CloudSyncService cloudSyncService
    protected final DoorStateService doorStateService

    @Inject
    DoorService(CloudSyncService cloudSyncService, DoorStateService doorStateService) {
        this.cloudSyncService = cloudSyncService
        this.doorStateService = doorStateService
    }

    SendMessageResult lock(String door) {
        return cloudSyncService.sendSqsMessage(request(door).lock())
    }

    List<SendMessageResult> lock() {
        return requestAll{ String door -> request(door).lock()}
    }

    SendMessageResult unlock(String door) {
        return cloudSyncService.sendSqsMessage(request(door).unlock())
    }

    List<SendMessageResult> unlock() {
        return requestAll{ String door -> request(door).unlock()}
    }

    SendMessageResult grantAccess(String door) {
        return cloudSyncService.sendSqsMessage(request(door).grantAccess())
    }

    List<SendMessageResult> grantAccess() {
        return requestAll{ String door -> request(door).grantAccess()}
    }

    SendMessageResult stopAlarm(String door) {
        return cloudSyncService.sendSqsMessage(requestControl(door).stopAlarm())
    }

    List<SendMessageResult> stopAlarm() {
        return requestAll{ String door -> requestControl(door).stopAlarm()}
    }

    //~ BEGIN PROTECTED METHODS ===============================================

    protected List<SendMessageResult> requestAll(Closure<VertXRequest> cls) {
        List<SendMessageResult> results = []
        doorStateService.doorNames.each{ String door ->
            results << cloudSyncService.sendSqsMessage(cls(door))

        }
        return results
    }

    protected ControlRequest requestControl(String door) {
        return (ControlRequest) new ControlRequest().forDoor(door)
    }

    protected DoorRequest request(String door) {
        return (DoorRequest) new DoorRequest().forDoor(door)
    }

}
