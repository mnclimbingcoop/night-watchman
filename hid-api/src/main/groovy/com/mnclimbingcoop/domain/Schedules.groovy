package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

import groovy.transform.CompileStatic

@CompileStatic
class Schedules extends AbstractEntityCollection {

    @JacksonXmlProperty(isAttribute=true)
    Integer scheduleID

    @JacksonXmlProperty(isAttribute=true)
    Integer schedulesInUse

    @JacksonXmlProperty(isAttribute=true)
    Integer totalSchedules

    @Override
    Integer getInUse() { schedulesInUse }

    @Override
    Integer getTotal() { totalSchedules }

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Schedule')
    List<Schedule> schedules

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='hid:Schedule')
    Schedule schedule

}
