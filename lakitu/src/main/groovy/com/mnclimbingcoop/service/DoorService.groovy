package com.mnclimbingcoop.service

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.domain.VertXRequest
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

    List<SendMessageResult> lock() {
        List<SendMessageResult> results = []
        doorStateService.doorNames.each{ String door ->
            results << cloudSyncService.sendSqsMessage(request(door).lock())
        }
        return results
    }

    SendMessageResult lock(String door) {
        return cloudSyncService.sendSqsMessage(request(door).lock())
    }

    List<SendMessageResult> unlock() {
        List<SendMessageResult> results = []
        doorStateService.doorNames.each{ String door ->
            results << cloudSyncService.sendSqsMessage(request(door).unlock())

        }
        return results
    }

    SendMessageResult unlock(String door) {
        return cloudSyncService.sendSqsMessage(request(door).unlock())
    }

    SendMessageResult grantAccess(String door) {
        return cloudSyncService.sendSqsMessage(request(door).grantAccess())
    }

    DoorRequest request(String door) {
        return (DoorRequest) new DoorRequest().forDoor(door)
    }

}
