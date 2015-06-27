package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

import java.time.LocalDateTime

@CompileStatic
class AlertAction {

    @JacksonXmlProperty(isAttribute=true)
    Integer eventCode

    @JacksonXmlProperty(isAttribute=true)
    String actionType

    @JacksonXmlProperty(isAttribute=true)
    String value

}
