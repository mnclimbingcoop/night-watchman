package com.mnclimbingcoop.domain.type

import groovy.transform.CompileStatic

@CompileStatic
enum State {

    SET('set', true),
    UNSET('unset', false)

    final String displayValue
    final boolean value

    State(String displayValue, boolean value) {
        this.displayValue = displayValue
        this.value = value
    }

    @JsonValue
    String toString() {
        displayValue
    }

    @JsonCreator
    static State fromString(String value) {
        if (value == null) { return null }
        State type = values().find {
            it.displayValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${State}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }

}
