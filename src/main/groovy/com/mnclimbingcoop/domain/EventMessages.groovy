package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

class EventMessages {

    @JacksonXmlProperty(isAttribute=true)
    String action

    @JacksonXmlProperty(isAttribute=true)
    Integer historyRecordMarker

    @JacksonXmlProperty(isAttribute=true)
    Long historyTimestamp

    @JacksonXmlProperty(isAttribute=true)
    Integer recordCount

    @JacksonXmlProperty(isAttribute=true)
    Boolean moreRecords

    @JacksonXmlProperty(isAttribute=true)
    Integer currentRecordMarker

    @JacksonXmlProperty(isAttribute=true)
    Long currentTimestamp

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='EventMessage')
    List<EventMessage> eventMessages

}
