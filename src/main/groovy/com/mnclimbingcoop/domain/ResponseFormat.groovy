package com.mnclimbingcoop.domain

import groovy.transform.CompileStatic

@CompileStatic
enum ResponseFormat {

    EXPANDED('expanded')

    final String xmlValue

    ResponseFormat(String xmlValue) {
        this.xmlValue = xmlValue
    }

    @JsonValue
    String toString() {
        xmlValue
    }

    @JsonCreator
    static ResponseFormat fromString(String value) {
        if (value == null) { return null }
        ResponseFormat type = values().find {
            it.xmlValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${ResponseFormat}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }

}
