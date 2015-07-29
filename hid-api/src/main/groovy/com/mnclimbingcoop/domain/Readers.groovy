package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

class Readers {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    Integer readerID = 1

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Reader')
    List<Reader> readers

}
