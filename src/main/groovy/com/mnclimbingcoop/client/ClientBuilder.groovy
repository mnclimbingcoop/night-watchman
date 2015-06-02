package com.mnclimbingcoop.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder

import groovy.transform.CompileStatic

import retrofit.RestAdapter
import retrofit.converter.Converter
import retrofit.converter.JacksonConverter

@CompileStatic
class ClientBuilder {

    String uri = 'http://localhost:8080'
    ObjectMapper objectMapper = new ObjectMapperBuilder().buildXml()

    RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.BASIC

    ClientBuilder withEndpoint(String uri) {
        this.uri = uri
        return this
    }

    ClientBuilder withObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper
        return this
    }

    @SuppressWarnings('UnnecessaryPublicModifier')
    public <T> T build(Class<T> clazz) {
        Converter converter = new JacksonConverter(objectMapper)
        RestAdapter.Builder builder = new RestAdapter.Builder()
                                                     .setConverter(converter)
                                                     .setEndpoint(uri)
                                                     .setLogLevel(logLevel)

        RestAdapter restAdapter = builder.build()

        return (T) restAdapter.create(clazz)
    }
}
