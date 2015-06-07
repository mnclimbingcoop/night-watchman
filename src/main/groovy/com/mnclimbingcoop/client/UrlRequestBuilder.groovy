package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Actions
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXMessage

import java.time.LocalDateTime
import java.time.ZoneOffset

class UrlRequestBuilder {

    final XmlMapper objectMapper
    final XmlRequestBuilder xmlBuilder

    UrlRequestBuilder(XmlMapper objectMapper) {
        this.objectMapper = objectMapper
        this.xmlBuilder = new XmlRequestBuilder()
    }

    String buildReport() {
        objectMapper.writeValueAsString(super.buildReport())
    }

    String displayRecent() {
        objectMapper.writeValueAsString(super.displayRecent())
    }

    String listRecent() {
        objectMapper.writeValueAsString(super.listRecent())
    }

    String listRecent(LocalDateTime since, Integer marker = 0, Integer records = 0) {
        objectMapper.writeValueAsString(super.listRecent(since, marker, records))
    }

    String doorStatus() {
        objectMapper.writeValueAsString(super.doorStatus())
    }

    String lockDoor() {
        objectMapper.writeValueAsString(super.lockDoor())
    }

    String unlockDoor() {
        objectMapper.writeValueAsString(super.unlockDoor())
    }

    String grantAccess() {
        objectMapper.writeValueAsString(super.grantAccess())
    }
}
