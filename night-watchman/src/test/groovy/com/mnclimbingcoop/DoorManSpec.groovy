package com.mnclimbingcoop

import com.mnclimbingcoop.service.DoorService
import spock.lang.Specification

class DoorManSpec extends Specification {

    DoorMan doorMan

    protected DoorService doorService

    void setup() {
        doorService = Mock()

        doorMan = new DoorMan(doorService)
    }

    void 'secure checks doors'() {
        when:
        doorMan.secure()

        then:
        1 * doorService.list()
        0 * _
    }


}
