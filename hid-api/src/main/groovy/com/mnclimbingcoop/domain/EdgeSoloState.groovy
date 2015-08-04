package com.mnclimbingcoop.domain

import java.util.concurrent.ConcurrentSkipListSet

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

}
