package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.DoorCommand
import com.mnclimbingcoop.domain.type.ResponseFormat

class Doors {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    DoorCommand command

    @JacksonXmlProperty(isAttribute=true)
    ResponseFormat responseFormat

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Door')
    List<Door> doors

    @JsonIgnore
    Door getDoor() {
        return doors[0]
    }

}
