package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class AlertActions {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='AlertAction')
    List<AlertAction> alertActions

}
