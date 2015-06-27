package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class EdgeSoloParameters {

    @JacksonXmlProperty(isAttribute=true)
    Action action

}
