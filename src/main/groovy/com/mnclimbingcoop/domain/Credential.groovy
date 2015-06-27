package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

import java.time.LocalDateTime

@CompileStatic
class Credential {

    @JacksonXmlProperty(isAttribute=true)
    String rawCardNumber

    @JacksonXmlProperty(isAttribute=true)
    Boolean isCard

    @JacksonXmlProperty(isAttribute=true)
    Integer cardNumber

    @JacksonXmlProperty(isAttribute=true)
    Integer formatID

    @JacksonXmlProperty(isAttribute=true)
    String formatName

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime endTime

    @JacksonXmlProperty(isAttribute=true)
    String cardholderID

    @JacksonXmlProperty(isAttribute=true)
    Boolean extendedAccess

    @JacksonXmlProperty(isAttribute=true)
    Boolean exemptFromPassback

    @JacksonXmlProperty(isAttribute=true)
    Boolean pinCommands

    @JacksonXmlProperty(isAttribute=true)
    Boolean confirmingPinExempt

}
