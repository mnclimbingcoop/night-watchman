package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import org.joda.time.LocalDateTime

class EdgeSoloParameters {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    String companyName

    @JacksonXmlProperty(isAttribute=true)
    String companyAddress

    @JacksonXmlProperty(isAttribute=true)
    String companyCity

    @JacksonXmlProperty(isAttribute=true)
    String companyState

    @JacksonXmlProperty(isAttribute=true)
    String companyCountry

    @JacksonXmlProperty(isAttribute=true)
    String companyEmail

    @JacksonXmlProperty(isAttribute=true)
    String companyTelephone

    @JacksonXmlProperty(isAttribute=true)
    String customHeading1

    @JacksonXmlProperty(isAttribute=true)
    String customHeading2

    @JacksonXmlProperty(isAttribute=true)
    String language

    @JacksonXmlProperty(isAttribute=true)
    Integer dateFormat

    @JacksonXmlProperty(isAttribute=true)
    Integer timeFormat

    @JacksonXmlProperty(isAttribute=true)
    Integer nameFormat

    @JacksonXmlProperty(isAttribute=true)
    Integer weekFormat

    @JacksonXmlProperty(isAttribute=true)
    String edgeSoloVersion

    @JacksonXmlProperty(isAttribute=true)
    String doorName

    @JacksonXmlProperty(isAttribute=true)
    Integer encryptTLS

    @JacksonXmlProperty(isAttribute=true)
    Integer acceptEULA

    @JacksonXmlProperty(isAttribute=true)
    Integer installerLock

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime currentDateTime

}
