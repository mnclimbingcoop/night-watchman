package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

import groovy.transform.CompileStatic

@CompileStatic
@JacksonXmlRootElement(localName='VertXMessage')
class VertXRequest {

    @JacksonXmlProperty(localName='hid:AlertActions')
    AlertActions alerts

    @JacksonXmlProperty(localName='hid:CardFormats')
    CardFormats cardFormats

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

}
