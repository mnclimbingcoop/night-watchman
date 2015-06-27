package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class CardFormat {

    @JacksonXmlProperty(isAttribute=true)
    Integer formatID

    @JacksonXmlProperty(isAttribute=true)
    String formatName

    @JacksonXmlProperty(isAttribute=true)
    Boolean isTemplate

    @JacksonXmlProperty(isAttribute=true)
    Integer templateID

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='FixedField')
    List<FixedField> cardFormats

}
