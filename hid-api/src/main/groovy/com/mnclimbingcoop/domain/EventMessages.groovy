package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

class EventMessages extends AbstractEntityCollection implements Comparable<EventMessages> {

    @JacksonXmlProperty(isAttribute=true)
    Integer eventsInUse

    @JacksonXmlProperty(isAttribute=true)
    Integer totalEvents

    @JacksonXmlProperty(isAttribute=true)
    Integer currentRecordMarker

    @JacksonXmlProperty(isAttribute=true)
    Integer historyRecordMarker

    @JacksonXmlProperty(isAttribute=true)
    Long currentTimestamp

    @JacksonXmlProperty(isAttribute=true)
    Long historyTimestamp

    @Override
    String toString() {
        String str = ''
        if (eventsInUse) { str += "eventsInUse=${eventsInUse} " }
        if (totalEvents) { str += "totalEvents=${totalEvents} " }
        if (currentRecordMarker) { str += "currentRecordMarker=${currentRecordMarker} " }
        if (historyRecordMarker) { str += "historyRecordMarker=${historyRecordMarker} " }
        if (currentTimestamp) { str += "currentTimestamp=${currentTimestamp} " }
        if (historyTimestamp) { str += "historyTimestamp=${historyTimestamp} " }
        return str
    }

    @Override
    Integer getInUse() { eventsInUse }

    @Override
    Integer getTotal() { totalEvents }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='EventMessage')
    List<EventMessage> eventMessages

    @Override
    int compareTo(EventMessages other) {
        if (this.historyTimestamp != other.historyTimestamp) {
            return this.historyTimestamp <=> other.historyTimestamp
        }
        return this.historyRecordMarker <=> other.historyRecordMarker
    }

}
