package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime

class DoorHealth {

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

    boolean isOk() {
        LocalDateTime now = LocalDateTime.now()
        LocalDateTime minCheckTime = now.minusMinutes(1)

        return (
            lastDoorCheck > minCheckTime &&
            lastEventCheck > minCheckTime &&
            doorOk &&
            eventOk &&
            otherOk
        )
    }

}
