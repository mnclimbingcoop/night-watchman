package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.health.DoorHealth
import com.mnclimbingcoop.health.EventHealth
import com.mnclimbingcoop.health.Health

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Value

@CompileStatic
@Named
@Slf4j
class HealthService {

    protected final Health health = new Health()

    Set<String> LOCALHOST_ADDRESSES = [
            '0:0:0:0:0:0:0:1',
            '0:0:0:0:0:0:0:1%lo',
            'fe80:0:0:0:0:0:0:1%lo0',
            '127.0.0.1'
    ] as Set

    @Inject
    HealthService(@Value('${server.port}') Integer serverPort) {
        // Set port
        health.port = serverPort

        // Set addresses
        NetworkInterface.networkInterfaces.each { NetworkInterface inf ->
            inf.inetAddresses.each{ InetAddress inet ->
                if (!(inet.hostAddress in LOCALHOST_ADDRESSES)) {
                    health.addresses << inet.hostAddress
                }
            }
        }
    }

    Health getCurrentHealth() {
        return health
    }

    void heartbeat() {
        health.heartbeatCount++
    }

    void updateDependentHealth(Health depHealth) {
        health.dependentHealth = depHealth
        // Null out doors and events once we have dependentHealth
        health.doors = null
        health.events = null
    }

    void checkedMessages(int n) {
        LocalDateTime now = LocalDateTime.now()
        health.sqsHealth.lastQueueCheck = now
        if (n > 0) {
            health.sqsHealth.messagesReceived += n
            health.sqsHealth.lastQueueRead = now
        }
        health.sqsHealth.sqsReadOk = true
    }

    void checkMessagesFailed() {
        LocalDateTime now = LocalDateTime.now()
        health.sqsHealth.lastQueueCheck = now
        health.sqsHealth.sqsReadOk = false
    }

    void sentMessage() {
        LocalDateTime now = LocalDateTime.now()
        health.sqsHealth.messagesSent++
        health.sqsHealth.lastQueueWritten = now
        health.sqsHealth.sqsWriteOk = true
    }

    void sendMessageFailed() {
        health.sqsHealth.sqsWriteOk = false
    }

    void initDoor(String name, String address) {
        if (health.doors.containsKey(name)) {
            health.doors[name].name = name
            health.doors[name].address = address
        } else {
            health.doors[name] = new DoorHealth(name: name, address: address)
        }

        if (health.events.containsKey(name)) {
            health.events[name].name = name
            health.events[name].address = address
        } else {
            health.events[name] = new EventHealth(name: name, address: address)
        }
    }

    void checkedDoor(String name) {
        LocalDateTime now = LocalDateTime.now()
        health.doors[name].lastDoorCheck = now
        health.doors[name].doorOk = true
    }

    void updatedDoor(String name) {
        LocalDateTime now = LocalDateTime.now()
        health.doors[name].lastDoorCheck = now
        health.doors[name].lastDoorMessage = now
        health.doors[name].doorOk = true
    }

    void checkedEvents(String name) {
        LocalDateTime now = LocalDateTime.now()
        health.events[name].lastEventCheck = now
        health.events[name].eventOk = true
    }

    void updatedEvents(String name) {
        LocalDateTime now = LocalDateTime.now()
        health.events[name].lastEventCheck = now
        health.events[name].lastEvent = now
        health.events[name].eventOk = true
    }

    void getFailed(String name, VertXRequest request, String message) {
        if (request.doors) {
            health.doors[name].doorOk = false
        } else if (request.eventMessages) {
            health.events[name].eventOk = false
        } else {
            health.doors[name].otherOk = false
        }
        health.doors[name].errorMessage = message
    }

    void getSucceded(String name, VertXRequest request) {
        if (request.doors) {
            health.doors[name].doorOk = true
        } else if (request.eventMessages) {
            health.events[name].eventOk = true
        } else {
            health.doors[name].otherOk = true
        }
    }

}
