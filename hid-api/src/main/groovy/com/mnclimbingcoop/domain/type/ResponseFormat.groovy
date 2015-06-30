package com.mnclimbingcoop.domain.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

import groovy.transform.CompileStatic

@CompileStatic
enum ResponseFormat {

    EXPANDED('expanded'),
    STATUS('status'),
    CUSTOMIZED('customized')

    final String displayValue

    ResponseFormat(String displayValue) {
        this.displayValue = displayValue
    }

    @JsonValue
    String toString() {
        displayValue
    }

    @JsonCreator
    static ResponseFormat fromString(String value) {
        if (value == null) { return null }
        ResponseFormat type = values().find {
            it.displayValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${ResponseFormat}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }

}
