package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

import groovy.transform.CompileStatic

@CompileStatic
class Credentials extends AbstractEntityCollection {

    @JacksonXmlProperty(isAttribute=true)
    String rawCardNumber

    @JacksonXmlProperty(isAttribute=true)
    Boolean isCard

    @JacksonXmlProperty(isAttribute=true)
    Integer credentialsInUse

    @JacksonXmlProperty(isAttribute=true)
    Integer totalCredentials

    @JacksonXmlProperty(isAttribute=true)
    Integer unassignedCredentials

    @Override
    Integer getInUse() { credentialsInUse }

    @Override
    Integer getTotal() { totalCredentials }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Credential')
    List<Credential> credentials

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='hid:Credential')
    Credential credential

}
