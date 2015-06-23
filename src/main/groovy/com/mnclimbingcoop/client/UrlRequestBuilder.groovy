package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.domain.VertXMessage

import groovy.transform.CompileStatic

import java.time.LocalDateTime

import javax.inject.Inject
import javax.inject.Named

@Named
@CompileStatic
class UrlRequestBuilder {

    final XmlMapper objectMapper
    final XmlRequestBuilder xmlBuilder

    @Inject
    UrlRequestBuilder(XmlMapper objectMapper) {
        this.objectMapper = objectMapper
        this.xmlBuilder = new XmlRequestBuilder()
    }

    String buildReport() {
        wrap(xmlBuilder.buildReport())
    }

    String displayRecent() {
        wrap(xmlBuilder.displayRecent())
    }

    String listRecent() {
        wrap(xmlBuilder.listRecent())
    }

    String listRecent(LocalDateTime since, Integer marker = 0, Integer records = 0) {
        wrap(xmlBuilder.listRecent(since, marker, records))
    }

    String doorStatus() {
        wrap(xmlBuilder.doorStatus())
    }

    String lockDoor() {
        wrap(xmlBuilder.lockDoor())
    }

    String unlockDoor() {
        wrap(xmlBuilder.unlockDoor())
    }

    String grantAccess() {
        wrap(xmlBuilder.grantAccess())
    }

    String listUsers(String offset = 0, String count = 10) {
        wrap(xmlBuilder.listUsers(offset, count))
    }

    protected String wrap(VertXMessage message) {
        objectMapper.writeValueAsString(message)
    }

}
