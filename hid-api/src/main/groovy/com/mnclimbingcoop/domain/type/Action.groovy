package com.mnclimbingcoop.domain.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

import groovy.transform.CompileStatic

@CompileStatic
enum Action {

    ADD_DATA('AD', 'Add Data'),
    COMMAND_MODE('CM', 'Command Mode'),
    DELETE_DATA('DD', 'Delete Data'),
    DESCRIBE_RECORDS('DR', 'Describe Records'),
    RESPONSE_DATA('RD', 'Response Data'),
    LIST_RECORDS('LR', 'List Records'),
    RESPONSE_LIST('RL', 'Response List'),
    RECORD_SET('RS', 'Record Set'),
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
