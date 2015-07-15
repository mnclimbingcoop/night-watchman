package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class Reader implements Comparable<Reader> {

    @JacksonXmlProperty(isAttribute=true)
    String readerName

    @JacksonXmlProperty(isAttribute=true)
    Integer interfaceID

    @JacksonXmlProperty(isAttribute=true)
    Integer readerAddress

    @JacksonXmlProperty(isAttribute=true)
    Integer accessMethod

    @JacksonXmlProperty(isAttribute=true)
    Integer readerType

    @JacksonXmlProperty(isAttribute=true)
    Integer antipassbackType

    @JacksonXmlProperty(isAttribute=true)
    Integer antipassbackTimeoutSec

    @JacksonXmlProperty(isAttribute=true)
    Integer antipassbackAction

    @JacksonXmlProperty(isAttribute=true)
    Boolean supportPinCommands

    @JacksonXmlProperty(isAttribute=true)
    Integer pinSize

    @JacksonXmlProperty(isAttribute=true)
    Integer pinEntryTimeoutSec

    @JacksonXmlProperty(isAttribute=true)
    Integer terminationCode

    @JacksonXmlProperty(isAttribute=true)
    Integer allowedAttempts

    @JacksonXmlProperty(isAttribute=true)
    Integer lockoutTimeSec

    @JacksonXmlProperty(isAttribute=true)
    Integer clearPinBufferCode

    @JacksonXmlProperty(isAttribute=true)
    Integer keypadType

    @Override
    int compareTo(Reader other) {
        return this.interfaceID <=> other.interfaceID
    }
}
