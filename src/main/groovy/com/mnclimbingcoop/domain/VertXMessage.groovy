package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement()
class VertXMessage {

    @JacksonXmlProperty(localName='EventMessages')
    EventMessages eventMessages

}
