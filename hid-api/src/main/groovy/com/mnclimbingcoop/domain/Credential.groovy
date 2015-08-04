package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import org.joda.time.LocalDateTime

class Credential implements Comparable<Credential> {

    @JacksonXmlProperty(isAttribute=true)
    String rawCardNumber

    @JacksonXmlProperty(isAttribute=true)
    Boolean isCard

    @JacksonXmlProperty(isAttribute=true)
    String cardNumber

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

    @Override
    int compareTo(Credential other) {
        return this.rawCardNumber <=> other.rawCardNumber
    }
}
