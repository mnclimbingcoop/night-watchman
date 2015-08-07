package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.EventRequest

import java.util.concurrent.TimeUnit

import org.springframework.scheduling.annotation.Async

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import rx.Observable
import rx.Subscriber

@CompileStatic
@Named
@Slf4j
class EventService {

    protected final HealthService healthService
    protected final HidService hidService

    @Inject
    EventService(HealthService healthService, HidService hidService) {
        this.healthService = healthService
        this.hidService = hidService
    }

    @Async
    void watch() {

        log.info "preparing to watch for events."
        Thread.sleep(2000) // Wait 2s before starting
        log.info "watching for events."

        List<Observable> observables = hidService.doors.collect{
            observeEvents(it).buffer(1, TimeUnit.SECONDS)
        }

        Observable.merge(observables).subscribe(
            { List<EventMessage> events ->
                events.each{ EventMessage event ->
                    // credentials are already added to the state via the CredentialService
                    log.debug "Event: timestamp: ${event.timestamp} eventType: ${event.eventType} " +
                              "commandStatus: ${event.commandStatus} " +
                              "cardholderID: ${event.cardholderID} " +
                              "forename: ${event.forename} surname: ${event.surname}"
                }

                if (events) {
                    sync(events)
                }
            }, { Throwable t ->
                log.error "Error while reading events ${t.class}", t
            }, {
                log.info "Event stream stop."
            }
        )
    }

    protected Observable<EventMessage> observeEvents(String door) {
        return Observable.create({ Subscriber<EventMessage> subscriber ->
            Thread.start {
                VertXRequest request = new EventRequest().overview()
                EventMessages last = null
                while (!subscriber.unsubscribed) {
                    EventMessages overview = hidService.get(door, request)?.eventMessages

                    // If the overview has changed since last we checked...
                    if (overview && overview != last) {
                        VertXRequest latestEvents = new EventRequest().fromOverview(overview).since(last)
                        EventMessages latest = hidService.get(door, latestEvents)?.eventMessages
                        healthService.checkedEvents(door)
                        if (latest?.eventMessages) {
                            for (EventMessage eventMessage : latest.eventMessages) {
                                if (subscriber.unsubscribed) { break }
                                eventMessage.door = door
                                subscriber.onNext(eventMessage)
                            }
                            healthService.updatedEvents(door)
                        }
                        last = overview
                    } else {
                        // If we found events that we need to get, only wait 1/2 second before checking again
                        Thread.sleep(500)
                    }

                    // Else wait 2 seconds
                    Thread.sleep(2000)
                }
            }
        } as Observable.OnSubscribe<EventMessage>)
    }

    void sync(List<EventMessage> messages) {
        String door = messages[0].door
        EdgeSoloState state = new EdgeSoloState(
            doorName: door,
            events:   messages.toSet()
        )
        state.events.addAll(messages)
        hidService.sync(state)
    }

}

