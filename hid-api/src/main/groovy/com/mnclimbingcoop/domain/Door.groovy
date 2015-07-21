package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.State

import groovy.transform.CompileStatic

import org.joda.time.LocalDateTime

@CompileStatic
class Door implements Comparable<Door> {

    @JacksonXmlProperty(isAttribute=true)
    String doorName

    @JacksonXmlProperty(isAttribute=true)
    Integer doorUnlockScheduleID

    @JacksonXmlProperty(isAttribute=true)
    Integer doorUnlockGuardTime

    @JacksonXmlProperty(isAttribute=true)
    State guardTimeFlag

    @JacksonXmlProperty(isAttribute=true)
    State tamperAlarmState

    @JacksonXmlProperty(isAttribute=true)
    State acAlarmState

    @JacksonXmlProperty(isAttribute=true)
    State batteryAlarmState

    @JacksonXmlProperty(isAttribute=true)
    State doorHeldAlarmState

    @JacksonXmlProperty(isAttribute=true)
    State doorForcedAlarmState

    @JacksonXmlProperty(isAttribute=true)
    State relayState

    @JacksonXmlProperty(isAttribute=true)
    State auxRelayState

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime currentDateTime

    @JacksonXmlProperty(isAttribute=true)
    State relayAlertState

    boolean changed(Door other) {
        return (!other ||
                this.acAlarmState         != other.acAlarmState ||
                this.auxRelayState        != other.auxRelayState ||
                this.batteryAlarmState    != other.batteryAlarmState ||
                this.doorForcedAlarmState != other.doorForcedAlarmState ||
                this.doorHeldAlarmState   != other.doorHeldAlarmState ||
                this.relayAlertState      != other.relayAlertState ||
                this.relayState           != other.relayState ||
                this.tamperAlarmState     != other.tamperAlarmState)
    }

    @Override
    int compareTo(Door other) {
        return this.doorName <=> other.doorName
    }
}
