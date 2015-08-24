package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName='VertXMessage')
class VertXRequest {

    /** Used for remote requests */
    @JacksonXmlProperty(isAttribute=true)
    String doorName

    @JacksonXmlProperty(localName='hid:AlertActions')
    AlertActions alerts

    @JacksonXmlProperty(localName='hid:CardFormats')
    CardFormats cardFormats

    @JacksonXmlProperty(localName='hid:ControlVariables')
    ControlVariables controlVariables

    @JacksonXmlProperty(localName='hid:Cardholders')
    Cardholders cardholders

    @JacksonXmlProperty(localName='hid:Credentials')
    Credentials credentials

    @JacksonXmlProperty(localName='hid:Doors')
    Doors doors

    @JacksonXmlProperty(localName='hid:EdgeSoloParameters')
    EdgeSoloParameters parameters

    @JacksonXmlProperty(localName='hid:EventMessages')
    EventMessages eventMessages

    @JacksonXmlProperty(localName='hid:Readers')
    Readers readers

    @JacksonXmlProperty(localName='hid:Reports')
    Reports reports

    @JacksonXmlProperty(localName='hid:Schedules')
    Schedules schedules

    @JacksonXmlProperty(localName='hid:System')
    System system

    @JacksonXmlProperty(localName='hid:Time')
    Time time

    /** Not actually sent to door, but used for cloud to local */
    AccessHolder accessHolder

    VertXRequest forDoor(String door) {
        this.doorName = door
        return this
    }
}
