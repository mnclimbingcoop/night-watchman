package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class FixedField {

    @JacksonXmlProperty(isAttribute=true)
    Integer value

}
