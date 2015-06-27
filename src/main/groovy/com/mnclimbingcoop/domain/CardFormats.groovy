package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

import groovy.transform.CompileStatic

@CompileStatic
class CardFormats {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    ResponseFormat responseFormat

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='CardFormat')
    List<CardFormat> cardFormats

}
