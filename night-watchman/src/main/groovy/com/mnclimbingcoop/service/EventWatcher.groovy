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

import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers

@CompileStatic
@Slf4j
class EventWatcher {

    protected final HealthService healthService
    protected final HidService hidService
    protected final String door

    EventWatcher(String door, HealthService healthService, HidService hidService) {
        this.door = door
        this.healthService = healthService
        this.hidService = hidService
    }

    @Async
    void watch() {
        log.info "observing ${door} door for events."
        observeEvents().observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(
            { EventMessage event ->
                hidService.hidStates[door].events << event
                // credentials are already added to the state via the CredentialService
                log.info("Event: door: ${event.door} " +
                         "timestamp: ${event.timestamp} " +
                         "eventType: ${event.eventType} " +
                         "commandStatus: ${event.commandStatus} " +
                         "cardholderID: ${event.cardholderID} " +
                         "forename: ${event.forename} " +
                         "surname: ${event.surname}")

                sync([event])
            }, { Throwable t ->
                log.error "Error while reading events ${t.class}", t
            }, {
                log.info "Event stream stop."
            }
        )
    }

    protected Observable<EventMessage> observeEvents() {
        return Observable.create({ Subscriber<EventMessage> subscriber ->
            VertXRequest request = new EventRequest().overview()
            EventMessages last = null
            while (!subscriber.unsubscribed) {
                try {
                    EventMessages overview = hidService.get(door, request)?.eventMessages
                    healthService.checkedEvents(door)

                    // If the overview has changed since last we checked...
                    if (overview && overview != last) {
                        VertXRequest latestEvents = new EventRequest().fromOverview(overview).since(last)
                        EventMessages latest = hidService.get(door, latestEvents)?.eventMessages

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
                    // Else wait 2 seconds between event checks
                    Thread.sleep(2000)
                } catch (HidRemoteErrorException ex) {
                    log.error "HID Error ${ex.message}, retrying."
                } catch (Exception ex) {
                    log.error 'Unknown Error, retrying.', ex
                }
            }
        } as Observable.OnSubscribe<EventMessage>)
    }

    void sync(List<EventMessage> messages) {
        EdgeSoloState state = new EdgeSoloState(
            doorName: door,
            events:   messages.toSet()
        )
        state.events.addAll(messages)
        hidService.sync(state)
    }

}

