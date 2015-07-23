package com.mnclimbingcoop

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.datatype.joda.JodaModule

import com.fasterxml.jackson.annotation.JsonInclude.Include

// import com.fasterxml.jackson.datatype.jsr310.JSR310Module
class ObjectMapperBuilder {

    ObjectMapper build() {
        ObjectMapper objectMapper = new ObjectMapper()

        objectMapper.registerModule(new JodaModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false)
                    .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
                    .configure(SerializationFeature.INDENT_OUTPUT, true)
                    .setSerializationInclusion(Include.NON_NULL)

        return objectMapper
    }

    XmlMapper buildXml() {

        XmlMapper objectMapper = new XmlMapper()

        objectMapper.registerModule(new JodaModule())
        objectMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.INDENT_OUTPUT, true)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false)
                    .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)
                    .setSerializationInclusion(Include.NON_NULL)

        return objectMapper

    }
}
