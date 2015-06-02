package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

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

    @JacksonXmlProperty(localName='EventMessage')
    List<EventMessage> eventMessages = []

}
