package com.mnclimbingcoop

import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.State
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.EventType
import com.mnclimbingcoop.domain.type.State

import org.joda.time.LocalDateTime

class XmlResponseSpec extends XmlSpecification {

    void 'xml marshalling from read door'() {
        given:
        String expected = xmlFromFixture('response/door/list-door')
        println expected

        when:
        VertXResponse parsed = objectMapper.readValue(expected, VertXResponse)

        then:
        parsed.doors.action == Action.RESPONSE_DATA
        parsed.doors.doors[0].doorName == 'HID Edge Solo'
        parsed.doors.doors[0].relayState == State.UNSET
    }

    void 'xml marhalling from read log'() {
        given:
        String expected = xmlFromFixture('response/event/list-events1')
        VertXResponse message = new VertXResponse(
            eventMessages: new EventMessages(
                action: Action.RESPONSE_LIST,
                historyRecordMarker: 2232,
                historyTimestamp:  1428535349,
                recordCount: 1,
                moreRecords: false,
                eventMessages: [
                    new EventMessage(
                        cardholderID: 1,
                        forename: 'Aaron',
                        surname: 'Zirbes',
                        eventType: EventType.GRANTED_ACCESS,
                        timestamp: LocalDateTime.parse('2015-04-08T20:02:37')
                    )
                ]

            )
        )

        when:
        String xml = objectMapper.writeValueAsString(message)

        and:
        VertXResponse parsed = objectMapper.readValue(expected, VertXResponse)
        String back = objectMapper.writeValueAsString(parsed)

        then:
        xml == back
    }
}
