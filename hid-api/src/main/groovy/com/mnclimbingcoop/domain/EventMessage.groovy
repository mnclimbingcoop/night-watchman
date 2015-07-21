package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

import org.joda.time.LocalDateTime

@CompileStatic
class EventMessage implements Comparable<EventMessage> {

    // Stolen from the JS Source code
    static final Set<Integer> RED_EVENTS = [
            1022,
            1023,
            2024,
            2029,
            2036,
            2042,
            2043,
            2046,
            4041,
            4042,
            4043,
            4044,
            4045
    ].asSet().asImmutable()

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    String forename

    @JacksonXmlProperty(isAttribute=true)
    String surname

    // 2020 == Access Granted
    // 12032 == Unlock Door
    // from en_EN.js ???
    @JacksonXmlProperty(isAttribute=true)
    Integer eventType

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime timestamp

    @JacksonXmlProperty(isAttribute=true)
    Boolean commandStatus

    boolean isRedEvent() {
        return eventType in RED_EVENTS
    }

    @Override
    String toString() {
        String str = ''
        if (cardholderID) { str += "cardholderID=${cardholderID} " }
        if (forename) { str += "forename=${forename} " }
        if (surname) { str += "surname=${surname} " }
        if (eventType) { str += "eventType=${eventType} " }
        if (timestamp) { str += "timestamp=${timestamp} " }
        return str
    }

    @Override
    int compareTo(EventMessage other) {
        return this.timestamp <=> other.timestamp
    }
}
