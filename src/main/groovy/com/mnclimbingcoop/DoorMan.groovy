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

    protected final DoorService doorService

    @Inject
    DoorMan(DoorService doorService) {
        this.doorService = doorService
    }

    @Scheduled(fixedRate = 10000l)
    void ping() {
        doorService.getDoorStatus()
    }
}
