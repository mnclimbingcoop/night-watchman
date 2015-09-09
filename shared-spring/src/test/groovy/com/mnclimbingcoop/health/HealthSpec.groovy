package com.mnclimbingcoop.health

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.service.HealthService

import org.joda.time.LocalDateTime

import spock.lang.Specification

class HealthSpec extends Specification {

    HealthService piHealthService
    HealthService lakituHealthService
    static final String DOOR = 'test'
    ObjectMapper objectMapper = ObjectMapperBuilder.build()

    void setup() {
        piHealthService = new HealthService(8000)
        lakituHealthService = new HealthService(8000)
    }

    void 'healthcheck passes for lakitu best case'() {
        given:
        VertXRequest doorRequest = new VertXRequest(doors: new Doors())
        VertXRequest eventRequest = new VertXRequest(eventMessages: new EventMessages())
        VertXRequest otherRequest = new VertXRequest()

        when: 'the raspberry pi does the things it needs'
        piHealthService.checkedMessages(10)
        piHealthService.sentMessage()
        piHealthService.initDoor(DOOR, '127.0.0.1')
        piHealthService.checkedDoor(DOOR)
        piHealthService.checkedEvents(DOOR)
        piHealthService.heartbeat()
        piHealthService.getSucceded(DOOR, doorRequest)
        piHealthService.getSucceded(DOOR, eventRequest)
        piHealthService.getSucceded(DOOR, otherRequest)

        and: 'get pi health'
        Health pi = piHealthService.health
        String piJson = objectMapper.writeValueAsString(pi)
        println "PI: ${piJson}"



        and: 'lakitu does the things it does'
        lakituHealthService.checkedMessages(1)
        lakituHealthService.sentMessage()
        lakituHealthService.updateDependentHealth(pi)

        and: 'get lakitu health'
        Health lakitu = lakituHealthService.health
        String lakituJson = objectMapper.writeValueAsString(lakitu)
        println "LAKITU: ${lakituJson}"

        then:
        pi.ok
        lakitu.ok

    }

    void 'healthcheck fails if pi has not checked sqs queue in at least 1 minute'() {
        given:
        LocalDateTime now = LocalDateTime.now()
        VertXRequest doorRequest = new VertXRequest(doors: new Doors())
        VertXRequest eventRequest = new VertXRequest(eventMessages: new EventMessages())
        VertXRequest otherRequest = new VertXRequest()

        when: 'the raspberry pi does the things it needs'
        piHealthService.checkedMessages(10)
        piHealthService.sentMessage()
        piHealthService.initDoor(DOOR, '127.0.0.1')
        piHealthService.checkedDoor(DOOR)
        piHealthService.checkedEvents(DOOR)
        piHealthService.heartbeat()
        piHealthService.getSucceded(DOOR, doorRequest)
        piHealthService.getSucceded(DOOR, eventRequest)
        piHealthService.getSucceded(DOOR, otherRequest)

        and: 'pi health hasn not checked SQS in a while'
        Health pi = piHealthService.health
        pi.sqsHealth.lastQueueCheck = now.minusMinutes(3)

        String piJson = objectMapper.writeValueAsString(pi)
        println "Sick PI: ${piJson}"

        and: 'lakitu does the things it does'
        lakituHealthService.checkedMessages(1)
        lakituHealthService.sentMessage()
        lakituHealthService.updateDependentHealth(pi)

        and: 'get lakitu health'
        Health lakitu = lakituHealthService.health
        String lakituJson = objectMapper.writeValueAsString(lakitu)
        println "Sick LAKITU: ${lakituJson}"

        then:
        !pi.sqsHealth.ok
        !pi.ok
        !lakitu.ok

    }

}
