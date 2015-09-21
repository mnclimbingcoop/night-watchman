package com.mnclimbingcoop.wiegand

class WiegandEncoder {

    static String encode(Integer facilityCode, Integer cardNumber) {
        // get the binary representation of the facility and card codes
        String binaryFacilityCode = Integer.toBinaryString(facilityCode).padLeft(8, '0')
        String binaryCardNumber = Integer.toBinaryString(cardNumber).padLeft(16, '0')

        // Build the binary string for the card code
        String rawCardNumber  = "${binaryFacilityCode}${binaryCardNumber}"
        // calculate the left 13 digit parity check
        boolean leftParity = false
        rawCardNumber[0..12].each{ if (it == "1") { leftParity = ! leftParity } }
        String l = leftParity ? '1' : '0'
        // calculate the right 13 digit parity check
        boolean rightParity = true
        rawCardNumber[13..-1].each{ if (it == "1") { rightParity = ! rightParity } }

        println "encode: rawCardNumber: ${rawCardNumber}"

        String r = rightParity ? '1' : '0'
        // build the binary string w/ the parity bits
        String binaryWiegand = "${l}${rawCardNumber}${r}"
        println "encode: binaryWiegand: ${binaryWiegand}"
        BigInteger cardData = Integer.parseInt(binaryWiegand, 2)
        // return the hex version
        return cardData.toString(16).toUpperCase().padLeft(8, '0')
    }

    static List<Integer> decode(String wiegand) {
        // Convert to int
        BigInteger intWiegand = new BigInteger(wiegand, 16)
        // Convert int to 26 bit binary string
        String binaryWiegand = intWiegand.toString(2).padLeft(26, '0')

        println "decode: binaryWiegand: ${binaryWiegand}"

        // Ignore parity bits
        String rawCardNumber = binaryWiegand[1..-2]
        println "decode: rawCardNumber: ${rawCardNumber}"

        // get the binary representation of the facility and card codes
        String binaryFacilityCode = rawCardNumber[0..7]
        String binaryCardNumber = rawCardNumber[8..-1]

        // Build the binary string for the card code
        Integer facilityCode = Integer.parseInt(binaryFacilityCode, 2)
        Integer cardNumber = Integer.parseInt(binaryCardNumber, 2)

        // return the two parts
        return [ facilityCode, cardNumber ]
    }

}
