package com.mnclimbingcoop.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class Cardholder implements Comparable<Cardholder> {

    @JacksonXmlProperty(isAttribute=true)
    Integer cardholderID

    @JacksonXmlProperty(isAttribute=true)
    String forename

    @JacksonXmlProperty(isAttribute=true)
    String middleName

    @JacksonXmlProperty(isAttribute=true)
    String surname

    @JacksonXmlProperty(isAttribute=true)
    String email

    @JacksonXmlProperty(isAttribute=true)
    String phone

    @JacksonXmlProperty(isAttribute=true)
    String custom1

    @JacksonXmlProperty(isAttribute=true)
    String custom2

    @JacksonXmlProperty(isAttribute=true)
    Integer roleSetID

    @JacksonXmlProperty(isAttribute=true)
    Boolean pinCommands

    @JacksonXmlProperty(isAttribute=true)
    String confirmingPin

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

    @Override
    int compareTo(Cardholder other) {
        return this.cardholderID <=> other.cardholderID
    }
}
