package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

import org.joda.time.LocalDateTime

class EventRequest extends VertXRequest {

    EventRequest() {
        eventMessages = new EventMessages(action: Action.LIST_RECORDS)
    }

    EventRequest overview() {
        eventMessages = new EventMessages(action: Action.LIST_RECORDS)
        return this
    }

    EventRequest recent() {
        eventMessages = new EventMessages(action: Action.DESCRIBE_RECORDS)
        return this
    }

    EventRequest listSince(LocalDateTime historyTimestamp, Integer count = 10) {

        Long timestamp = toTimestamp(historyTimestamp)

        eventMessages = new EventMessages(
            action: Action.LIST_RECORDS,
            historyTimestamp: timestamp,
            recordCount: count
        )
        return this
    }

    EventRequest list(Integer historyRecordMarker,
                      LocalDateTime historyTimestamp,
                      Integer count = 10) {

        Long timestamp = toTimestamp(historyTimestamp)
        eventMessages = new EventMessages(
            action: Action.LIST_RECORDS,
            historyRecordMarker: historyRecordMarker,
            historyTimestamp: timestamp,
            recordCount: count
        )
        return this
    }

    Long toTimestamp(LocalDateTime time) {
        return time.toDate().time / 1000
    }
}
