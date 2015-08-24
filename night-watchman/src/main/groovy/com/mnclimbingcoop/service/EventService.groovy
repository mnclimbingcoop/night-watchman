package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.EventMessage
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.EventRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class EventService {

    protected final HidService hidService

    @Inject
    EventService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, EventMessages> poll() {

        // Check the overview status for each door
        VertXRequest request = new EventRequest().overview()
        hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.eventMessages) {
                EventMessages overview = resp.eventMessages
                EventMessages last = hidService.hidStates[name].eventOverview

                // If the overview has changed since last we checked...
                if (overview != last) {
                    log.debug 'Events not up to date, requesting latest events.'
                    VertXRequest latestEvents = new EventRequest().fromOverview(overview).since(last)
                    VertXResponse response = hidService.get(name, latestEvents)
                    if (response.eventMessages?.eventMessages) {

                        // Add new events
                        hidService.hidStates[name].events.addAll(response.eventMessages.eventMessages)
                        // Push events to the cloud
                        sync(name, overview, response.eventMessages.eventMessages)
                    }
                }

                // Update the state so it knows that we have the latest overview info now
                hidService.hidStates[name].eventOverview = overview

                return [ name, overview ]
            } else {
                return [ name, null ]
            }
        }
    }

    // add inventory builder, or only keep last 100 events?

    void sync(String door, EventMessages overview, List<EventMessage> messages) {
        EdgeSoloState state = new EdgeSoloState(
            doorName:      door,
            eventOverview: overview,
            events:        messages.toSet()
        )
        state.events.addAll(messages)
        hidService.sync(state)
    }

    void sync() {
        hidService.sync()
    }

}

