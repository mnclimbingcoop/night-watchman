package com.mnclimbingcoop

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature

class ObjectMapperBuilder {

    ObjectMapper build() {
        ObjectMapper objectMapper = new ObjectMapper()

        //.registerModule( new JSR310Module())
        objectMapper.findAndRegisterModules()

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false)
                    .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
                    .configure(SerializationFeature.INDENT_OUTPUT, true)

        return objectMapper
    }
}
