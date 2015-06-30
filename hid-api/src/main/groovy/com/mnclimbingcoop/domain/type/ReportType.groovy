package com.mnclimbingcoop.domain.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

import groovy.transform.CompileStatic

@CompileStatic
enum ReportType {

    LOCK('events')

    final String xmlValue

    ReportType(String xmlValue) {
        this.xmlValue = xmlValue
    }

    @JsonValue
    String toString() {
        xmlValue
    }

    @JsonCreator
    static ReportType fromString(String value) {
        if (value == null) { return null }
        ReportType type = values().find {
            it.xmlValue.equalsIgnoreCase(value) || it.name().equalsIgnoreCase(value)
        }
        if (!type && (value != 'null')) {
            String errorMessage = "No matching '${ReportType}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return type
    }

}
