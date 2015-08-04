package com.mnclimbingcoop

import com.mnclimbingcoop.request.DoorRequest
import com.mnclimbingcoop.request.EventRequest

import org.joda.time.LocalDateTime

import spock.lang.IgnoreRest

class XmlRequestSpec extends XmlSpecification {

    void 'event overview'() {
        expect:
        xmlFromFixture('request/event/list-overview-events') == toXML(new EventRequest().overview())
    }

    void 'event list single'() {
        given:
        LocalDateTime time = new LocalDateTime(2015, 4, 9, 1, 2, 37)

        expect:
        xmlFromFixture('request/event/list-events1') == toXML(new EventRequest().list(2233, time, 1))
    }

    void 'event list multi'() {
        given:
        LocalDateTime time = new LocalDateTime(2015, 6, 26, 23, 21, 14)

        expect:
        xmlFromFixture('request/event/list-events2') == toXML(new EventRequest().list(4327, time, 100))
    }

    void 'lock door'() {
        expect:
        xmlFromFixture('request/door/lock-door') == toXML(new DoorRequest().lock())
    }

    void 'unlock door'() {
        expect:
        xmlFromFixture('request/door/unlock-door') == toXML(new DoorRequest().unlock())
    }

    void 'grant access to door'() {
        expect:
        xmlFromFixture('request/door/open-door') == toXML(new DoorRequest().grantAccess())
    }

}
