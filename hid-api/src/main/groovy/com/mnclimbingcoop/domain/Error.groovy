package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import groovy.transform.CompileStatic


@CompileStatic
class Error {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    String elementType

    @JacksonXmlProperty(isAttribute=true)
    String errorCode

    @JacksonXmlProperty(isAttribute=true)
    String errorReporter

    @JacksonXmlProperty(isAttribute=true)
    String errorMessage

    String toString() {
        return "action=${action} " +
               "elementType=${elementType} " +
               "errorCode=${errorCode} " +
               "errorMessage=${errorMessage} " +
               "errorReporter=${errorReporter}"

    }

}
