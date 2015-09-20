package com.mnclimbingcoop.wiegand

import spock.lang.Specification
import spock.lang.Unroll

class WiegandEncoderSpec extends Specification {

    @Unroll
    void 'can encode #cardNumber card for facility code #facilityCode'() {
        expect:
        println ''
        wiegand == WiegandEncoder.encode(facilityCode, cardNumber)
        [facilityCode, cardNumber] == WiegandEncoder.decode(wiegand)

        where:
        facilityCode | cardNumber || wiegand
        0            |          0 || '00000001'
        1            |          1 || '02020002'
        15           |      65432 || '021FFF31'
        63           |          1 || '007E0002'
        63           |      65535 || '027FFFFE'
        178          |      10001 || '03644E22'
        178          |      41212 || '016541F9'
        178          |      41510 || '0165444D'
        254          |          4 || '03FC0008'
        254          |      65535 || '01FDFFFE'
    }
}
