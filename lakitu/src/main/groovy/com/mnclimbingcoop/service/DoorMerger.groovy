package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.EdgeSoloState

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class DoorMerger {

    static void merge(EdgeSoloState destination, EdgeSoloState source) {

        if (destination.doorName != source.doorName) {
            log.error "destination door name=${destination.doorName} does not match " +
                      "source door name=${source.doorName}"
            return
        }

        if (source.alerts) {
            log.debug "adding ${source.alerts.size()} alerts."
            destination.alerts.clear()
            destination.alerts.addAll(source.alerts)
        }
        if (source.cardFormats) {
            log.debug "adding ${source.cardFormats.size()} cardFormats."
            destination.cardFormats.clear()
            destination.cardFormats.addAll(source.cardFormats)
        }
        if (source.cardholders) {
            log.debug "adding ${source.cardholders.size()} cardholders."
            destination.cardholders.clear()
            destination.cardholders.addAll(source.cardholders)
        }
        if (source.credentials) {
            log.debug "adding ${source.credentials.size()} credentials."
            destination.credentials.clear()
            destination.credentials.addAll(source.credentials)
        }
        if (source.parameters) {
            log.debug "adding parameters."
            destination.parameters = source.parameters
        }
        if (source.readers) {
            log.debug "adding ${source.readers.size()} readers."
            destination.readers.clear()
            destination.readers.addAll(source.readers)
        }
        if (source.schedules) {
            log.debug "adding ${source.schedules.size()} schedules."
            destination.schedules.clear()
            destination.schedules.addAll(source.schedules)
        }
        if (source.system) {
            log.debug "adding system."
            destination.system = source.system
        }
        if (source.doors) {
            log.debug "adding ${source.doors.size()} doors."
            destination.doors.clear()
            destination.doors.addAll(source.doors)
        }

        if (source.eventOverview) {
            log.debug "adding eventOverview."
            destination.eventOverview = source.eventOverview
        }
        if (source.events) {
            log.debug "adding ${source.events.size()} events."
            // Events never clear, only get added
            destination.events.addAll(source.events)
        }

    }

}
