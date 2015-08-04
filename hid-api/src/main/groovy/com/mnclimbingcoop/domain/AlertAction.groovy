package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class AlertAction implements Comparable<AlertAction> {

    @JacksonXmlProperty(isAttribute=true)
    Integer eventCode

    @JacksonXmlProperty(isAttribute=true)
    String actionType

    @JacksonXmlProperty(isAttribute=true)
    String value

    int compareTo(AlertAction other) {
        return this.eventCode <=> other.eventCode
    }

}
