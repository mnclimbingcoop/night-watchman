package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Actions
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXMessage

import java.time.LocalDateTime
import java.time.ZoneOffset

class RequestBuilder {

    VertXMessage buildReport() {
        return new VertXMessage(
            reportRequest: new Reports(
                action: Actions.COMMAND_MODE, type: 'events'
            )
        )
    }

    VertXMessage displayRecent() {
        return new VertXMessage(
            logRequest: new EventMessages(action: Actions.DISPLAY_RECENT)
        )
    }

    VertXMessage listRecent() {
        return new VertXMessage(
            logRequest: new EventMessages(action: Actions.LIST_RECENT)
        )
    }

    VertXMessage listRecent(LocalDateTime since, Integer marker = 0, Integer records = 0) {
        VertXMessage message = new VertXMessage(
            logRequest: new EventMessages(
                action: Actions.LIST_RECENT,
                historyTimestamp: since.toEpochSecond(ZoneOffset.UTC)
            )
        )
        if (marker) { message.logRequest.historyRecordMarker = marker }
        if (records) { message.logRequest.recordCount = records }

        return message
    }

    VertXMessage lockDoor() {
        return new VertXMessage(
            doorRequest: new Doors(action: Actions.COMMAND_MODE, command: 'lockDoor')
        )
    }

    VertXMessage unlockDoor() {
        return new VertXMessage(
            doorRequest: new Doors(action: Actions.COMMAND_MODE, command: 'unlockDoor')
        )
    }

    VertXMessage grantAccess() {
        return new VertXMessage(
            doorRequest: new Doors(action: Actions.COMMAND_MODE, command: 'grantAccess')
        )
    }
}
