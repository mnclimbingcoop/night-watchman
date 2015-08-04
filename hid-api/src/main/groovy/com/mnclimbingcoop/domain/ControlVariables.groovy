package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import org.joda.time.LocalDateTime

class ControlVariables {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    Integer index

    @JacksonXmlProperty(isAttribute=true)
    Boolean value

}
