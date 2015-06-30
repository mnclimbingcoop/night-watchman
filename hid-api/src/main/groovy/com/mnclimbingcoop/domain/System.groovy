package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import groovy.transform.CompileStatic

import org.joda.time.LocalDate

@CompileStatic
class System {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    LocalDate buildDate

    @JacksonXmlProperty(isAttribute=true)
    Integer internalID

    @JacksonXmlProperty(isAttribute=true)
    String vertXModel

    @JacksonXmlProperty(isAttribute=true)
    Integer buildNumber

    @JacksonXmlProperty(isAttribute=true)
    String OEMModel

    @JacksonXmlProperty(isAttribute=true)
    String vertXRelease

    @JacksonXmlProperty(isAttribute=true)
    String acVersion

    @JacksonXmlProperty(isAttribute=true)
    Boolean isUserPasswordSet

    @JacksonXmlProperty(isAttribute=true)
    Boolean isAdminPasswordSet

    @JacksonXmlProperty(isAttribute=true)
    String languages

}
