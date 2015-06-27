package com.mnclimbingcoop.domain.type

import groovy.transform.CompileStatic

@CompileStatic
enum Action {

    COMMAND_MODE('CM', 'Command Mode'),
    DELETE_DATA('DD', 'Delete Data'),
    DISPLAY_RECENT('DR', 'Display Recent'),
    LIST_RECORDS('LR', 'List Records'),
    RESPONSE_LIST('RL', 'Response List'),
    UPDATE_DATA('UD', 'Update Data')

    final String xmlValue
    final String displayValue

    Action(String xmlValue, String displayValue) {
        this.displayValue = displayValue
        this.xmlValue = xmlValue
    }

    @JsonValue
    String toString() {
        xmlValue
    }

    @JsonCreator
    static Action fromString(String value) {
        if (value == null) { return null }
        Action type = values().find {
            it.xmlValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${Action}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }

}
