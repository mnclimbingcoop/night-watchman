package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.EventType

import org.joda.time.LocalDateTime

class EventMessage implements Comparable<EventMessage> {

    /** Not part of HID API */
    @JacksonXmlProperty(isAttribute=true)
    String door

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    String forename

    @JacksonXmlProperty(isAttribute=true)
    String surname

    @JacksonXmlProperty(isAttribute=true)
    EventType eventType

    @JacksonXmlProperty(isAttribute=true)
    String getEventDescription() {
        eventType?.description
    }

    @JacksonXmlProperty(isAttribute=true)
    Boolean getEventWarn() {
        eventType?.warn
    }

    @JacksonXmlProperty(isAttribute=true)
    Boolean getEventAlert() {
        eventType?.alert
    }

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime oldTime

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime newTime

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime timestamp

    @JacksonXmlProperty(isAttribute=true)
    Integer ioState

    @JacksonXmlProperty(isAttribute=true)
    Boolean commandStatus

    @Override
    String toString() {
        String str = ''
        if (cardholderID) { str += "cardholderID=${cardholderID} " }
        if (forename) { str += "forename=${forename} " }
        if (surname) { str += "surname=${surname} " }
        if (eventType) { str += "eventType=${eventType} " }
        if (ioState) { str += "ioState=${ioState} " }
        if (oldTime) { str += "oldTime=${oldTime} " }
        if (newTime) { str += "newTime=${newTime} " }
        if (commandStatus) { str += "commandStatus=${commandStatus} " }
        if (timestamp) { str += "timestamp=${timestamp} " }
        if (eventType) { str += "eventDescription=${eventType.description} " }
        return str
    }

    @Override
    int compareTo(EventMessage other) {
        return this.timestamp <=> other.timestamp
    }
}
