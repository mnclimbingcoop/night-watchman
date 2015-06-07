package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class Doors {

    @JacksonXmlProperty(isAttribute=true)
    String action

    @JacksonXmlProperty(isAttribute=true)
    String command

    @JacksonXmlProperty(isAttribute=true)
    String responseFormat

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Door')
    List<Door> doors

    @JsonIgnore
    Door getDoor() {
        return doors[0]
    }

}
