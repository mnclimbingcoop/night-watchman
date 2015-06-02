package com.mnclimbingcoop

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.datatype.jsr310.JSR310Module

import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.State
import com.mnclimbingcoop.domain.VertXMessage

import java.time.LocalDateTime

import spock.lang.Specification


class XmlSpec extends Specification {

    ObjectMapper objectMapper

    void setup() {

        objectMapper = new XmlMapper()
                .registerModule(new JSR310Module())
                .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false)
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)


    }

    void 'xml marshalling from read door'() {
        given:
        String expected = xmlFromFixture('response/read-door1')

        when:
        VertXMessage parsed = objectMapper.readValue(expected, VertXMessage)

        then:
        parsed.doors.action == 'RD'
        parsed.doors.doors[0].doorName == 'HID Edge Solo'
        parsed.doors.doors[0].relayState == State.set
    }

    void 'xml marhalling from read log'() {
        given:
        String expected = xmlFromFixture('response/read-log1')
        VertXMessage message = new VertXMessage(
            eventMessages: new EventMessages(
                action: 'RL',
                historyRecordMarker: 2232,
                historyTimestamp:  1428535349,
                recordCount: 1,
                moreRecords: false,
                eventMessages: [
                    new EventMessage(
                        cardholderID: 2,
                        forename: 'Aaron',
                        surname: 'Zirbes',
                        eventType: 2020,
                        timestamp: LocalDateTime.parse('2015-04-08T20:02:37')
                    )
                ]

            )
        )

        when:
        String xml = objectMapper.writeValueAsString(message)

        and:
        VertXMessage parsed = objectMapper.readValue(expected, VertXMessage)
        String back = objectMapper.writeValueAsString(parsed)

        then:
        xml == back

    }

    protected String xmlFromFixture(String fixture) {
        String path = "/${fixture}.xml"
        return xmlFromResource(path)
    }

    protected String xmlFromResource(String resourcePath) {
        InputStream inputStream = this.class.getResourceAsStream(resourcePath)
        if (inputStream) {
            return inputStream.text
        }
        throw new FileNotFoundException(resourcePath)
    }


}
