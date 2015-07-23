package com.mnclimbingcoop.domain

import groovy.transform.CompileStatic
import java.util.concurrent.ConcurrentSkipListSet

@CompileStatic
class EdgeSoloState {

    String doorName

    // Inventory
    Set<AlertAction> alerts = new ConcurrentSkipListSet<AlertAction>()
    Set<CardFormat> cardFormats = new ConcurrentSkipListSet<CardFormat>()
    Set<Cardholder> cardholders = new ConcurrentSkipListSet<Cardholder>()
    Set<Credential> credentials = new ConcurrentSkipListSet<Credential>()
    EdgeSoloParameters parameters
    Set<Reader> readers = new ConcurrentSkipListSet<Reader>()
    Set<Schedule> schedules = new ConcurrentSkipListSet<Schedule>()
    System system

    // Door State
    Set<Door> doors = new ConcurrentSkipListSet<Door>()

    // Event Audits
    EventMessages eventOverview
    Set<EventMessage> events = new ConcurrentSkipListSet<EventMessage>()

    void merge(EdgeSoloState state) {

        if (this.doorName != state.doorName) { return }

        if (state.alerts) {
            this.alerts.clear()
            this.alerts.addAll(state.alerts)
        }
        if (state.cardFormats) {
            this.cardFormats.clear()
            this.cardFormats.addAll(state.cardFormats)
        }
        if (state.cardholders) {
            this.cardholders.clear()
            this.cardholders.addAll(state.cardholders)
        }
        if (state.credentials) {
            this.credentials.clear()
            this.credentials.addAll(state.credentials)
        }
        if (state.parameters) {
            this.parameters = state.parameters
        }
        if (state.readers) {
            this.readers.clear()
            this.readers.addAll(state.readers)
        }
        if (state.schedules) {
            this.schedules.clear()
            this.schedules.addAll(state.schedules)
        }
        if (state.system) {
            this.system = state.system
        }
        if (state.doors) {
            this.doors.clear()
            this.doors.addAll(state.doors)
        }

        if (state.eventOverview) {
            this.eventOverview = state.eventOverview
        }
        if (state.events) {
            // Events never clear, only get added
            this.events.addAll(state.events)
        }

    }

}
