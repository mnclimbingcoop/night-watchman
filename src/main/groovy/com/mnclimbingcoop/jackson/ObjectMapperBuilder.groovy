package com.mnclimbingcoop

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.datatype.jsr310.JSR310Module

import com.fasterxml.jackson.annotation.JsonInclude.Include

class ObjectMapperBuilder {

    ObjectMapper build() {
        ObjectMapper objectMapper = new ObjectMapper()

        objectMapper.findAndRegisterModules()

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false)
                    .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
                    .configure(SerializationFeature.INDENT_OUTPUT, true)

        return objectMapper
    }

    ObjectMapper buildXml() {

        ObjectMapper objectMapper = new XmlMapper()
                .registerModule(new JSR310Module())
                .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false)
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)

                objectMapper.setSerializationInclusion(Include.NON_NULL)

        return objectMapper

    }
}
