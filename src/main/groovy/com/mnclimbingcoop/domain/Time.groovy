package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class Time {

    @JacksonXmlProperty(isAttribute=true)
    Action action

    @JacksonXmlProperty(isAttribute=true)
    Integer month

    @JacksonXmlProperty(isAttribute=true)
    Integer dayOfMonth

    @JacksonXmlProperty(isAttribute=true)
    Integer year

    @JacksonXmlProperty(isAttribute=true)
    Integer hour

    @JacksonXmlProperty(isAttribute=true)
    Integer minute

    @JacksonXmlProperty(isAttribute=true)
    Integer second

    // "CST6CDT,M3.2.0/2,M11.1.0/2"
    @JacksonXmlProperty(localName='TZ' isAttribute=true)
    String tz

    // 062
    @JacksonXmlProperty(localName='TZCode' isAttribute=true)
    String tzCode

}
