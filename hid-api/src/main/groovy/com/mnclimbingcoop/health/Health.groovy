package com.mnclimbingcoop.health

class Health {

    // Local host access
    Set<String> addresses = [] as Set
    Integer port

    // SQS Checks
    SqsHealth sqsHealth = new SqsHealth()

    // Door Check Status
    Map<String, DoorHealth> doors = [:]

    boolean isOk() {
        return (
            sqsHealth.ok &&
            doors.size() > 0 &&
            doors.every{ k, v -> v.ok }
        )
    }
}
