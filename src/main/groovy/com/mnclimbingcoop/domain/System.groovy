package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import groovy.transform.CompileStatic

@CompileStatic
class System {

    @JacksonXmlProperty(isAttribute=true)
    Action action

}
