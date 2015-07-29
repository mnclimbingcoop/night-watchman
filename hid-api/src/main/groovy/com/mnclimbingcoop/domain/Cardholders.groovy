package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

class Cardholders extends AbstractEntityCollection {

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholdersInUse

    @JacksonXmlProperty(isAttribute=true)
    Integer totalCardholders

    @Override
    Integer getInUse() { cardholdersInUse }

    @Override
    Integer getTotal() { totalCardholders }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Cardholder')
    List<Cardholder> cardholders

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='hid:Cardholder')
    Cardholder cardholder

}
