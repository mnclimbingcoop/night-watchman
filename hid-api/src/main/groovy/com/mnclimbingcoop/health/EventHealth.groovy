package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime

class EventHealth extends AbstractHealth {

    String name
    String address

    LocalDateTime lastEventCheck
    LocalDateTime lastEvent
    boolean eventOk = false

    String errorMessage

    LocalDateTime now() {
        return LocalDateTime.now()
    }

    String getEventCheckDrift() {
        getDrift(lastEventCheck)
    }

    String getEventDrift() {
        getDrift(lastEvent)
    }

    boolean isOk() {
        LocalDateTime minCheckTime = now().minusMinutes(1)
        return ( lastEventCheck > minCheckTime && eventOk)
    }

}
