package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Error
import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse

import java.util.concurrent.atomic.AtomicLong

import org.joda.time.LocalDateTime

import spock.lang.Specification

class EventWatcherSpec extends Specification {

    static final String DOOR = 'DOOR'

    EventWatcher service
    HealthService healthService
    HidService hidService

    AtomicLong start = new AtomicLong(LocalDateTime.now().toDate().time)
    AtomicLong eventType = new AtomicLong(1)
    AtomicLong historyRecordMarker = new AtomicLong(1000)

    void setup() {
        healthService = Mock()
        hidService = Mock()
        service = new EventWatcher(DOOR, healthService, hidService)
    }

    void 'watch can survive a lot of errors'() {
        when:
        service.observeEvents().limit(2).toBlocking().toIterable().eachWithIndex{ EventMessage event, int i ->
            println "receiving event #${i}: ${event}"
        }

        then:
        7 * hidService.get(DOOR, _ as VertXRequest) >> { throw new HidRemoteErrorException('WAT', new Error()) }
        3 * hidService.get(DOOR, _ as VertXRequest) >> { throw new IllegalArgumentException('WAT') }
        4 * hidService.get(DOOR, _ as VertXRequest) >> { return newEventOverview() }
        2 * healthService.checkedEvents(DOOR)
        and: 'depending on when the thread exits, one or two times'
        (1..2) * healthService.updatedEvents(DOOR)
        0 * _
    }

    protected VertXResponse newEventOverview() {
        long time = start.incrementAndGet()
        println "getting new overview ${time}"
        new VertXResponse(
            eventMessages: new EventMessages(
                currentTimestamp: time,
                historyTimestamp: time,
                historyRecordMarker: historyRecordMarker.incrementAndGet(),
                eventMessages: [ new EventMessage(eventType: eventType.incrementAndGet()) ]
            )
        )
    }
}
