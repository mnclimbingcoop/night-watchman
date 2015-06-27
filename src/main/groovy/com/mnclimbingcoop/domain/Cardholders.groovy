package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

import groovy.transform.CompileStatic

@CompileStatic
class Cardholders {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    ResponseFormat responseFormat

    @JacksonXmlProperty(isAttribute=true)
    Integer recordOffset

    @JacksonXmlProperty(isAttribute=true)
    Integer recordCount

    @JacksonXmlProperty(isAttribute=true)
    Boolean moreRecords

    @JacksonXmlProperty(isAttribute=true)
    String cardholdersInUse

    @JacksonXmlProperty(isAttribute=true)
    String totalCardholders

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Cardholder')
    List<Cardholder> cardholders

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='hid:Cardholder')
    Cardholder cardholder

}
