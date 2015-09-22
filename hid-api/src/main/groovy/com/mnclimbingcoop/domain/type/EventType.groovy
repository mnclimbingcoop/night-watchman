package com.mnclimbingcoop.domain.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum EventType {

    ALARM_ACKNOWLEDGED(                  4034,  false, false, "Alarm Acknowledged"),
    DENIED_ACCESS_ACCESS_PIN_NOT_FOUND(  1023,   true, false, "Denied Access{6} Access PIN Not Found {3}"),
    DENIED_ACCESS_CARD_EXPIRED(          2036,   true, false, "Denied Access{6} Card Expired {2}"),
    DENIED_ACCESS_CARD_NOT_FOUND(        1022,   true, false, "Denied Access{6} Card Not Found {3}"),
    DENIED_ACCESS_PIN_EXPIRED(           2046,   true, false, "Denied Access - PIN Expired {2}"),
    DENIED_ACCESS_PIN_LOCKOUT(           2042,   true, false, "Denied Access{6} PIN Lockout {2}"),
    DENIED_ACCESS_SCHEDULE(              2024,   true, false, "Denied Access{6} Schedule {2}"),
    DENIED_ACCESS_UNASSIGNED_ACCESS_PIN( 2044,   true, false, "Denied Access{6} Unassigned Access PIN {3}"),
    DENIED_ACCESS_UNASSIGNED_CARD(       2043,   true, false, "Denied Access{6} Unassigned Card {3}"),
    DENIED_ACCESS_WRONG_PIN(             2029,   true, false, "Denied Access{6} Wrong PIN {2}"),
    DOOR_FORCED_ALARM(                   4041,   true,  true, "Door Forced Alarm"),
    DOOR_HELD_ALARM(                     4042,   true,  true, "Door Held Alarm"),
    DOOR_LOCKED(                         12033, false, false, "Door Locked"),
    DOOR_LOCKED_SCHEDULED(               4035,  false, false, "Door Locked-Scheduled"),
    DOOR_UNLOCKED(                       12032, false, false, "Door Unlocked"),
    DOOR_UNLOCKED_SCHEDULED(             4036,  false, false, "Door Unlocked-Scheduled"),
    GRANTED_ACCESS(                      2020,  false, false, "Granted Access{6} {2}"),
    GRANTED_ACCESS_EXTENDED_TIME(        2021,  false, false, "Granted Access{6} Extended Time {2}"),
    GRANTED_ACCESS_MANUAL(               12031, false, false, "Granted Access{6} Manual"),
    INPUT_A_ALARM(                       4044,   true,  true, "Input A Alarm"),
    INPUT_B_ALARM(                       4045,   true,  true, "Input B Alarm"),
    TAMPER_SWITCH_ALARM(                 4043,   true,  true, "Tamper Switch Alarm"),
    TIME_SET(                            7020,   true,  true, "Time Set to: {5}")

    final Integer code
    final boolean warn
    final boolean alert
    final String description

    EventType(Integer code, boolean warn, boolean alert, String description) {
        this.code = code
        this.warn = warn
        this.alert = alert
        this.description = description
    }

    String toString() { code.toString() }

    @JsonCreator
    static EventType fromString(String value) {
        if (value == null) { return null }
        EventType eventCode = values().find {
            it.code.toString() == value || it.name().equalsIgnoreCase(value)
        }
        if (!eventCode && (value != 'null')) {
            String errorMessage = "No matching '${EventType}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return eventCode
    }

    @JsonValue
    Integer toCode() { code }

    static EventType fromCode(Integer value) {
        if (!value) { return null }
        EventType eventCode = values().find { it.code == value }
        if (!eventCode && (value >= 0)) {
            String errorMessage = "No matching '${EventType}' found for '${value}' expected one of ${values()}"
            throw new IllegalArgumentException(errorMessage)
        }
        return eventCode
    }

}
