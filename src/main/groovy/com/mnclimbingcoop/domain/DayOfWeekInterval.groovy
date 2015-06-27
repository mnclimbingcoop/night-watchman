package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

import java.time.LocalTime

@CompileStatic
class DayOfWeekInterval {

    @JacksonXmlProperty(isAttribute=true)
    Integer dayOfWeek

    @JacksonXmlProperty(isAttribute=true)
    LocalTime startTime

    @JacksonXmlProperty(isAttribute=true)
    LocalTime endTime

}
