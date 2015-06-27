package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.domain.VertXRequest

import groovy.transform.CompileStatic

import java.time.LocalDateTime

import javax.inject.Inject
import javax.inject.Named

@Named
@CompileStatic
class UrlRequestBuilder {

    final XmlMapper objectMapper

    @Inject
    UrlRequestBuilder(XmlMapper objectMapper) {
        this.objectMapper = objectMapper
    }

    protected String wrap(VertXRequest message) {
        objectMapper.writeValueAsString(message)
    }

}
