package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Actions
import com.mnclimbingcoop.domain.Cardholders
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXRequest

import groovy.transform.CompileStatic

import java.time.LocalDateTime
import java.time.ZoneOffset

@CompileStatic
class XmlRequestBuilder {

    VertXRequest buildReport() {
        new VertXRequest(reports: new Reports( action: Actions.COMMAND_MODE, type: 'events'))
    }

    VertXRequest displayRecent() {
        new VertXRequest(messages: new EventMessages(action: Actions.DESCRIBE_RECORDS))
    }

    VertXRequest listRecent() {
        new VertXRequest(messages: new EventMessages(action: Actions.LIST_RECORDS))
    }

    VertXRequest listRecent(LocalDateTime since, Integer marker = 0, Integer records = 0) {
        VertXRequest message = new VertXRequest(
            messages: new EventMessages(
                action: Actions.LIST_RECORDS,
                historyTimestamp: since.toEpochSecond(ZoneOffset.UTC)
            )
        )
        if (marker) { message.messages.historyRecordMarker = marker }
        if (records) { message.messages.recordCount = records }

        return message
    }

    VertXRequest doorStatus() {
        new VertXRequest(doors: new Doors(action: Actions.LIST_RECORDS, responseFormat: 'status'))
    }

    VertXRequest lockDoor() {
        new VertXRequest(doors: new Doors(action: Actions.COMMAND_MODE, command: 'lockDoor'))
    }

    VertXRequest unlockDoor() {
        new VertXRequest(doors: new Doors(action: Actions.COMMAND_MODE, command: 'unlockDoor'))
    }

    VertXRequest grantAccess() {
        new VertXRequest(doors: new Doors(action: Actions.COMMAND_MODE, command: 'grantAccess'))
    }

    VertXRequest listUsers(Integer offset = 0, Integer count = 10) {
        new VertXRequest(cardholders: new Cardholders(
            action: Actions.LIST_RECORDS,
            responseFormat: 'expanded',
            recordOffset: offset,
            recordCount: count
        ))
    }

    VertXRequest stopAlarm() { /* TODO */ }

    VertXRequest getUser(String id) { /* TODO */ }

    VertXRequest updateUser() { /* TODO */ }

    VertXRequest createUser() { /* TODO */ }

    VertXRequest removeUser() { /* TODO */ }


    VertXRequest listCards() { /* TODO */ }

    VertXRequest getCard(String id) { /* TODO */ }

    VertXRequest bulkAddCards() { /* TODO */ }

    VertXRequest addCard() { /* TODO */ }

    VertXRequest removeCard() { /* TODO */ }



    VertXRequest listSchedules() { /* TODO */ }

    VertXRequest getSchedule(String id) { /* TODO */ }

    VertXRequest addSchedule() { /* TODO */ }

    VertXRequest removeSchedule() { /* TODO */ }

    VertXRequest setClock() { /* TODO */ }

}
