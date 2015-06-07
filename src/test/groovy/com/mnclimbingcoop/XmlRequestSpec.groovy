package com.mnclimbingcoop

import com.mnclimbingcoop.domain.VertXMessage

import java.time.LocalDateTime

import spock.lang.Unroll

class XmlRequestSpec extends XmlSpecification {

    XmlRequestBuilder builder
    UrlRequestBuilder urlBuilder

    void setup() {
        builder = new XmlRequestBuilder()
        urlBuilder = new UrlRequestBuilder(objectMapper)
    }

    @Unroll
    void 'xml marshal #fixture from #action message'() {
        given:
        String expected = xmlFromFixture("request/${fixture}")


        when:
        VertXMessage message = builder."${action}"()
        String xml = stripWhitespace(objectMapper.writeValueAsString(message))

        and: 'the UrlRequestBuilder wraps the method nicely'
        String query = stripWhitespace(urlBuilder."${action}"())

        then:
        xml == expected
        xml == query

        where:
        fixture              | action
        'create-events-file' | 'buildReport'
        'display-recent'     | 'displayRecent'
        'list-recent1'       | 'listRecent'
        'lock-door'          | 'lockDoor'
        'open-door'          | 'grantAccess'
        'unlock-door'        | 'unlockDoor'
    }


    void 'xml marshal list recent message'() {
        given:
        String expected = xmlFromFixture('request/list-recent2')
        LocalDateTime since = LocalDateTime.of(2015, 4, 9, 1, 2, 37)
        VertXMessage message = builder.listRecent(since, 2233, 1)

        when:
        String xml = stripWhitespace(objectMapper.writeValueAsString(message))

        then:
        xml == expected
    }

}
