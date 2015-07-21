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

    EventRequest fromOverview(EventMessages overview) {
        if (overview) {
            return listSince(overview.currentTimestamp, overview.currentRecordMarker)
        }
        return this
    }

    EventRequest listSince(LocalDateTime timestamp, Integer recordMarker,  Integer count = 100) {

        Long timestampInt = toTimestamp(timestamp)

        eventMessages = new EventMessages(
            action: Action.LIST_RECORDS,
            historyTimestamp: timestampInt,
            historyRecordMarker: recordMarker,
            recordCount: count
        )
        return this
    }

    EventRequest since(EventMessages lastOverview) {
        if (lastOverview) {
            int count = overview.historyRecordMarker - last.historyRecordMarker
            return withCount(count)
        }
        return this
    }

    EventRequest withCount(Integer count) {
        if (count > 0) {
            eventMessages.recordCount = count
        }
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
