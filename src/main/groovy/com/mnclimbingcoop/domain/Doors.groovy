package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
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
