package com.mnclimbingcoop.domain.type

import spock.lang.Specification
import spock.lang.Unroll

class EventTypeSpec extends Specification {

    @Unroll
    void 'can marshal #eventCode to EventType'() {
        given:
        Integer code = eventCode.code
        String description = eventCode.description

        when:
        EventType decode = EventType.fromCode(code)

        then:
        decode.code == code
        decode.description == description

        where:
        eventCode << EventType.values()

    }

}
