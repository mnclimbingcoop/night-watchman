package com.mnclimbingcoop

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mnclimbingcoop.client.ClientBuilder
import com.mnclimbingcoop.client.HidEdgeProApi
import com.mnclimbingcoop.config.DoorConfiguration
import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.domain.VertXMessage

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class DoorMan {

    protected final HidService hidService
    protected final UrlRequestBuilder requestBuilder

    @Inject
    DoorMan(HidService hidService, UrlRequestBuilder requestBuilder) {
        this.hidService = hidService
        this.requestBuilder = requestBuilder
    }

    @Scheduled(fixedRate = 5000l)
    void ping() {
        hidService.doors.each{ String name ->
            log.info '>> Getting door status'
            HidEdgeProApi api = hidService.getApi(name)
            VertXMessage response = api.get(requestBuilder.doorStatus())
            Door door = response.doors.door

            log.info "Door [${name}]: ${door.doorName} - ${door.relayState}"
        }
    }
}
