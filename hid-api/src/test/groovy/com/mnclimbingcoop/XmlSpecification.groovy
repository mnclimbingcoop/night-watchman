package com.mnclimbingcoop

import com.fasterxml.jackson.databind.ObjectMapper

import spock.lang.Specification

class XmlSpecification extends Specification {

    ObjectMapper objectMapper

    void setup() {
        objectMapper = new ObjectMapperBuilder().buildXml()
    }

    protected String stripWhitespace(String str) {
        return str.replaceAll(/\n */, ' ')
                  .replaceAll('> <', '><')
                  .replaceAll(/ *$/, '')
                  .replaceFirst(/^ */, '')
    }

    protected String xmlFromFixture(String fixture) {
        String path = "/${fixture}.xml"
        return xmlFromResource(path)
    }

    protected String toXML(Object obj) {
        stripWhitespace(objectMapper.writeValueAsString(obj))
    }

    protected String xmlFromResource(String resourcePath) {
        InputStream inputStream = this.class.getResourceAsStream(resourcePath)
        if (inputStream) {
            return stripWhitespace(inputStream.text)
        }
        throw new FileNotFoundException(resourcePath)
    }


}
