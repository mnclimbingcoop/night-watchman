package com.mnclimbingcoop.health

class Health {

    // Local host access
    Set<String> addresses = [] as Set
    Integer port

    // SQS Checks
    SqsHealth sqsHealth = new SqsHealth()

    Integer heartbeatCount = 0

    // Door Check Status
    Map<String, DoorHealth> doors = [:]

    Health dependentHealth

    boolean isOk() {
        if (dependentHealth) {
            return dependentHealth.ok && sqsHealth?.ok
        }
        return doors?.size() > 0 && doors?.every{ k, v -> v?.ok }
    }
}
