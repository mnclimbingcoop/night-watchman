package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime

class DoorHealth extends AbstractHealth {

    String name
    String address

    LocalDateTime lastDoorCheck
    LocalDateTime lastDoorMessage
    boolean doorOk = false

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

    boolean isOk() {
        LocalDateTime minCheckTime = now().minusMinutes(1)

        return (
            lastDoorCheck > minCheckTime &&
            doorOk &&
            otherOk
        )
    }

}
