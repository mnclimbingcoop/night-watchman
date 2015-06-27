package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

import java.time.LocalDateTime

@CompileStatic
class EventMessage {

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    String forename

    @JacksonXmlProperty(isAttribute=true)
    String surname

    @JacksonXmlProperty(isAttribute=true)
    Integer eventType

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime timestamp

}
