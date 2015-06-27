package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

class EventRequest extends VertXRequest {

    EventRequest() {
        eventMessages = new EventMessages(action: Action.LIST_RECORDS)
        return this
    }

    EventRequest overview() {
        eventMessages = new EventMessages(action: Action.LIST_RECORDS)
        return this
    }

    EventRequest recent() {
        eventMessages = new EventMessages(action: Action.DESCRIBE_RECORDS)
        return this
    }

    EventRequest list(Integer historyRecordMarker,
                      Integer historyTimestamp
                      Integer count = 10) {
        eventMessages = new EventMessages(
            action: Action.LIST_RECORDS,
            historyRecordMarker: historyRecordMarker,
            historyTimestamp: historyTimestamp,
            recordCount: count
        )
        return this
    }

}
