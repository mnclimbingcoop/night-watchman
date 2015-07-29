package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class CardFormat implements Comparable<CardFormat> {

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

    @Override
    int compareTo(CardFormat other) {
        return this.formatID <=> other.formatID
    }
}
