package com.mnclimbingcoop.domain

import groovy.transform.CompileStatic
import java.util.concurrent.ConcurrentSkipListSet

@CompileStatic
class EdgeSoloState {

    Set<AlertAction> alerts = new ConcurrentSkipListSet<AlertAction>()

    Set<CardFormat> cardFormats = new ConcurrentSkipListSet<CardFormat>()

    Set<Cardholder> cardholders = new ConcurrentSkipListSet<Cardholder>()

    Set<Credential> credentials = new ConcurrentSkipListSet<Credential>()

    Set<Door> doors = new ConcurrentSkipListSet<Door>()

    Set<EventMessage> events = new ConcurrentSkipListSet<EventMessage>()

    EdgeSoloParameters parameters

    Set<Reader> readers = new ConcurrentSkipListSet<Reader>()

    Set<Schedule> schedules = new ConcurrentSkipListSet<Schedule>()

    System system

}
