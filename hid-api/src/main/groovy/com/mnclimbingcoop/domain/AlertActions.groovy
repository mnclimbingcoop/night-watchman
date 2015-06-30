package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action

import groovy.transform.CompileStatic

@CompileStatic
class AlertActions extends AbstractEntityCollection {

    @Override
    Integer getInUse() { 0 }

    @Override
    Integer getTotal() { 0 }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='AlertAction')
    List<AlertAction> alertActions

}
