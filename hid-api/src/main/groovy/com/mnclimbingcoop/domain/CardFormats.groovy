package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

class CardFormats extends AbstractEntityCollection {

    @JacksonXmlProperty(isAttribute=true)
    Integer cardFormatID

    @JacksonXmlProperty(isAttribute=true)
    Integer cardFormatsInUse

    @JacksonXmlProperty(isAttribute=true)
    Integer totalCardFormats

    @Override
    Integer getInUse() { cardFormatsInUse }

    @Override
    Integer getTotal() { totalCardFormats }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='CardFormat')
    List<CardFormat> cardFormats

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='hid:CardFormat')
    CardFormat cardFormat

}
