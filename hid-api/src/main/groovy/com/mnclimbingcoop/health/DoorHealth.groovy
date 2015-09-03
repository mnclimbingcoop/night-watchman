package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime

class DoorHealth extends AbstractHealth {

    String name
    String address

    LocalDateTime lastDoorCheck
    LocalDateTime lastDoorMessage
    boolean doorOk = false

    LocalDateTime lastEventCheck
    LocalDateTime lastEvent
    boolean eventOk = false

    String errorMessage
    boolean otherOk = false

    LocalDateTime now() {
        return LocalDateTime.now()
    }

    String getDoorCheckDrift() {
        getDrift(lastDoorCheck)
    }

    String getDoorMessageDrift() {
        getDrift(lastDoorMessage)
    }

    String getEventCheckDrift() {
        getDrift(lastEventCheck)
    }

    String getEventDrift() {
        getDrift(lastEvent)
    }

    boolean isOk() {
        LocalDateTime minCheckTime = now().minusMinutes(1)

        return (
            lastDoorCheck > minCheckTime &&
            lastEventCheck > minCheckTime &&
            doorOk &&
            eventOk &&
            otherOk
        )
    }

}
