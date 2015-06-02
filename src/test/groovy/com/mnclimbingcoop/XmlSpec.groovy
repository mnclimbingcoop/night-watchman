package com.mnclimbingcoop

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXMessage

import java.time.LocalDateTime

import spock.lang.Specification


class XmlSpec extends Specification {

    ObjectMapper objectMapper

    void setup() {

        // XMLInputFactory input = new WstxInputFactory()
        // input.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE)
        // new ObjectMapper(new XmlFactory(input, new WstxOutputFactory()));

        objectMapper = new XmlMapper().configure(SerializationFeature.INDENT_OUTPUT, true)

    }

    void 'xml marhalling'() {
        given:
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
                        timestamp: new LocalDateTime('2015-04-08T20:02:37')
                    )
                ]

            )
        )
        String expected = xmlFromFixture('response/read-log1')

        when:
        String xml = objectMapper.writeValueAsString(message)
        Map data = objectMapper.readValue(expected, Map)

        println data

        then:
        xml == expected

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
