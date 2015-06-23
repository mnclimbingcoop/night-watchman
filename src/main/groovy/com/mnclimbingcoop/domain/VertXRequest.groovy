package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement()
class VertXRequest {

    @JacksonXmlProperty(localName='hid:EventMessages')
    EventMessages messages

    @JacksonXmlProperty(localName='hid:Doors')
    Doors doors

    @JacksonXmlProperty(localName='hid:Reports')
    Reports reports

    @JacksonXmlProperty(localName='hid:Cardholders')
    Cardholders cardholders

}
