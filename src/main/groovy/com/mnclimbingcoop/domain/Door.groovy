package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import java.time.LocalDateTime

class Door {

    @JacksonXmlProperty(isAttribute=true)
    String doorName="HID Edge Solo"

    @JacksonXmlProperty(isAttribute=true)
    Integer doorUnlockScheduleID="0"

    @JacksonXmlProperty(isAttribute=true)
    Integer doorUnlockGuardTime="5"

    @JacksonXmlProperty(isAttribute=true)
    State guardTimeFlag="set"

    @JacksonXmlProperty(isAttribute=true)
    State tamperAlarmState="unset"

    @JacksonXmlProperty(isAttribute=true)
    State acAlarmState="unset"

    @JacksonXmlProperty(isAttribute=true)
    State batteryAlarmState="unset"

    @JacksonXmlProperty(isAttribute=true)
    State doorHeldAlarmState="unset"

    @JacksonXmlProperty(isAttribute=true)
    State doorForcedAlarmState="unset"

    @JacksonXmlProperty(isAttribute=true)
    State relayState="set"

    @JacksonXmlProperty(isAttribute=true)
    State auxRelayState="unset"

    @JacksonXmlProperty(isAttribute=true)
    LocalDateTime currentDateTime

    @JacksonXmlProperty(isAttribute=true)
    State relayAlertState="unset"

}
