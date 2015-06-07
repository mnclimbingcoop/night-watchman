package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Actions
import com.mnclimbingcoop.domain.Doors
import com.mnclimbingcoop.domain.EventMessages
import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXMessage

import java.time.LocalDateTime
import java.time.ZoneOffset

class XmlRequestBuilder {

    VertXMessage buildReport() {
        new VertXMessage(reportRequest: new Reports( action: Actions.COMMAND_MODE, type: 'events'))
    }

    VertXMessage displayRecent() {
        new VertXMessage(logRequest: new EventMessages(action: Actions.DISPLAY_RECENT))
    }

    VertXMessage listRecent() {
        new VertXMessage(logRequest: new EventMessages(action: Actions.LIST_RECENT))
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


    VertXMessage doorStatus() {
        new VertXMessage(doorRequest: new Doors(action: Actions.LIST_RECENT, responseFormat: 'status'))
    }

    VertXMessage lockDoor() {
        new VertXMessage(doorRequest: new Doors(action: Actions.COMMAND_MODE, command: 'lockDoor'))
    }

    VertXMessage unlockDoor() {
        new VertXMessage(doorRequest: new Doors(action: Actions.COMMAND_MODE, command: 'unlockDoor'))
    }

    VertXMessage grantAccess() {
        new VertXMessage(doorRequest: new Doors(action: Actions.COMMAND_MODE, command: 'grantAccess'))
    }

    VertXMessage stopAlarm() { // TODO }

    VertXMessage listUsers() { // TODO }

    VertXMessage getUser(String id) { // TODO }

    VertXMessage updateUser() { // TODO }

    VertXMessage createUser() { // TODO }

    VertXMessage removeUser() { // TODO }


    VertXMessage listCards() { // TODO }

    VertXMessage getCard(String id) { // TODO }

    VertXMessage bulkAddCards() { // TODO }

    VertXMessage addCard() { // TODO }

    VertXMessage removeCard() { // TODO }



    VertXMessage listSchedules() { // TODO }

    VertXMessage getSchedule(String id) { // TODO }

    VertXMessage addSchedule() { // TODO }

    VertXMessage removeSchedule() { // TODO }

    VertXMessage setClock() { // TODO }

}
