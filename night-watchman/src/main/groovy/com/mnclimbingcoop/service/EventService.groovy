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
import rx.schedulers.Schedulers

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

        List<Observable> observables = hidService.doors.collect{
            observeEvents(it).buffer(1, TimeUnit.SECONDS).observeOn(Schedulers.io())
        }

        Observable.merge(observables).subscribe(
            { List<EventMessage> events ->
                events.each{ EventMessage event ->
                    // credentials are already added to the state via the CredentialService
                    log.info "Event: timestamp: ${event.timestamp} eventType: ${event.eventType} " +
                              "commandStatus: ${event.commandStatus} " +
                              "cardholderID: ${event.cardholderID} " +
                              "forename: ${event.forename} surname: ${event.surname}"
                }

                if (events) { sync(events) }
            }, { Throwable t ->
                log.error "Error while reading events ${t.class}", t
            }, {
                log.info "Event stream stop."
            }
        )
    }

    protected Observable<EventMessage> observeEvents(String door) {
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
                    log.error 'HID Error, retrying.'
                } catch (Exception ex) {
                    log.error 'Unknown Error, retrying.', ex
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

