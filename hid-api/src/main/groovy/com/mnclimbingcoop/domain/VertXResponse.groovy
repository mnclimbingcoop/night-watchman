package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

import groovy.transform.CompileStatic

@CompileStatic
@JacksonXmlRootElement(localName='VertXMessage')
class VertXResponse {

    @JacksonXmlProperty(localName='AlertActions')
    AlertActions alerts

    @JacksonXmlProperty(localName='CardFormats')
    CardFormats cardFormats

    @JacksonXmlProperty(localName='Cardholders')
    Cardholders cardholders

    @JacksonXmlProperty(localName='Credentials')
    Credentials credentials

    @JacksonXmlProperty(localName='Doors')
    Doors doors

    @JacksonXmlProperty(localName='Error')
    Error error

    @JacksonXmlProperty(localName='EventMessages')
    EventMessages eventMessages

    @JacksonXmlProperty(localName='EdgeSoloParameters')
    EdgeSoloParameters parameters

    @JacksonXmlProperty(localName='Readers')
    Readers readers

    @JacksonXmlProperty(localName='Schedules')
    Schedules schedules

    @JacksonXmlProperty(localName='System')
    System system

    @JacksonXmlProperty(localName='Time')
    Time time

}
