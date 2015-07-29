package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class Role {

    @JacksonXmlProperty(isAttribute=true)
    Integer roleID

    @JacksonXmlProperty(isAttribute=true)
    Integer scheduleID

    @JacksonXmlProperty(isAttribute=true)
    String scheduleName

    @JacksonXmlProperty(isAttribute=true)
    Integer resourceID

    @JacksonXmlProperty(isAttribute=true)
    String resourceName

}
