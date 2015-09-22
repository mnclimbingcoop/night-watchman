package com.mnclimbingcoop.health

import java.util.concurrent.ConcurrentHashMap

import org.joda.time.LocalDateTime

class Health extends AbstractHealth {

    // Local host access
    Set<String> addresses = [] as Set
    Integer port

    // SQS Checks
    SqsHealth sqsHealth = new SqsHealth()

    Integer heartbeatCount = 0

    // Door Check Status
    Map<String, DoorHealth> doors = new ConcurrentHashMap<String, DoorHealth>()

    // Door Event Status
    Map<String, EventHealth> events = new ConcurrentHashMap<String, EventHealth>()

    Health dependentHealth

    @Override
    boolean isOk() {
        if (dependentHealth) {
            return dependentHealth.ok && sqsHealth?.ok
        }
        return (
            sqsHealth?.ok &&
            doors?.size() > 0 &&
            doors?.every{ k, v -> v?.ok } &&
            events?.size() > 0 &&
            events?.every{ k, v -> v?.ok }
        )
    }
}
