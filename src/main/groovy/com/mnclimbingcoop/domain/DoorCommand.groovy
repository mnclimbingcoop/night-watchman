package com.mnclimbingcoop.domain

import groovy.transform.CompileStatic

@CompileStatic
enum DoorCommand {

    LOCK('lockDoor'),
    UNLOCK('unlockDoor'),
    GRANT('grantAccess')

    final String xmlValue

    DoorCommand(String xmlValue) {
        this.xmlValue = xmlValue
    }

    @JsonValue
    String toString() {
        xmlValue
    }

    @JsonCreator
    static DoorCommand fromString(String value) {
        if (value == null) { return null }
        DoorCommand type = values().find {
            it.xmlValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${DoorCommand}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }

}
