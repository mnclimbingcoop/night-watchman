package com.mnclimbingcoop.health

import com.fasterxml.jackson.annotation.JsonCreator
import groovy.transform.CompileStatic

@CompileStatic
enum HealthState {

    ANY('any', -1),
    UNKNOWN('unknown', 0),
    PASSING('passing', 1),
    WARNING('warning', 2),
    CRITICAL('critical', 3)

    final String displayValue
    final int severity

    HealthState(String displayValue, int severity) {
        this.displayValue = displayValue
        this.severity = severity
    }

    String toString() {
        displayValue
    }

    @JsonCreator
    static HealthState fromString(String value) {
        if (value == null) { return null }
        HealthState type = values().find {
            it.displayValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${HealthState}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }
}

