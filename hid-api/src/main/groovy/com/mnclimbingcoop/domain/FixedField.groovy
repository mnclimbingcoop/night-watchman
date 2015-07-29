package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class FixedField {

    @JacksonXmlProperty(isAttribute=true)
    Integer value

}
