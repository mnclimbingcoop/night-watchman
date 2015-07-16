package com.mnclimbingcoop.compresssion

import spock.lang.Specification

class StringCompressorSpec extends Specification {

    void 'can compress string'() {
        given:
        final String source = '{ "crawl": "A long time ago, in a galaxy far, far away...." }'

        when:
        String compressed = StringCompressor.compress(source)

        then:
        compressed != source

        when:
        String decompressed = StringCompressor.decompress(compressed)

        then:
        decompressed == source

    }
}
