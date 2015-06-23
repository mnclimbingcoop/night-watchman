package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

class Cardholders {

    @JacksonXmlProperty(isAttribute=true)
    String action

    @JacksonXmlProperty(isAttribute=true)
    String responseFormat

    @JacksonXmlProperty(isAttribute=true)
    Integer recordOffset

    @JacksonXmlProperty(isAttribute=true)
    Integer recordCount

    @JacksonXmlProperty(isAttribute=true)
    Boolean moreRecords

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Cardholder')
    List<Cardholder> cardholders

}
