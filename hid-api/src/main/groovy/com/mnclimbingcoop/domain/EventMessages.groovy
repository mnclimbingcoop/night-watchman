package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import groovy.transform.CompileStatic

@CompileStatic
class EventMessages extends AbstractEntityCollection {

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
    Integer getInUse() { eventsInUse }

    @Override
    Integer getTotal() { totalEvents }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='EventMessage')
    List<EventMessage> eventMessages

}
