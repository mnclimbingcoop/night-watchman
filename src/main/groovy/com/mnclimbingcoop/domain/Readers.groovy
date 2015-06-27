package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class Readers {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    Integer readerID = 1

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Reader')
    List<Reader> readers

}
