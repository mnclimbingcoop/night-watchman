package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

import java.time.LocalDateTime

class Cardholder {

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    String forename

    @JacksonXmlProperty(isAttribute=true)
    String surname

    @JacksonXmlProperty(isAttribute=true)
    Integer roleSetID

    @JacksonXmlProperty(isAttribute=true)
    Boolean pinCommands

    @JacksonXmlProperty(isAttribute=true)
    Boolean confirmingPinExempt

    @JacksonXmlProperty(isAttribute=true)
    Boolean exemptFromPassback

    @JacksonXmlProperty(isAttribute=true)
    Boolean extendedAccess

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Credential')
    List<Credential> credentials

    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName='Role')
    List<Role> roles

}
