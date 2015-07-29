package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

abstract class AbstractEntityCollection {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    ResponseFormat responseFormat

    @JacksonXmlProperty(isAttribute=true)
    Integer recordOffset

    @JacksonXmlProperty(isAttribute=true)
    Integer recordCount

    @JacksonXmlProperty(isAttribute=true)
    Boolean moreRecords

    abstract Integer getInUse()

    abstract Integer getTotal()

}
