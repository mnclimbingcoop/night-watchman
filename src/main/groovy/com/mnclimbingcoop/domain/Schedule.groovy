package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import groovy.transform.CompileStatic

@CompileStatic
class Schedule {

    @JacksonXmlProperty(isAttribute=true)
    Integer scheduleID

    @JacksonXmlProperty(isAttribute=true)
    String scheduleName

    @JacksonXmlProperty(isAttribute=true)
    Integer specialDaysInUse

    @JacksonXmlProperty(isAttribute=true)
    Integer peopleAssigned

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='DayOfWeekInterval')
    List<DayOfWeekInterval> dayOfWeekIntervals

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='hid:DayOfWeekInterval')
    List<DayOfWeekInterval> dayIntervals

}
