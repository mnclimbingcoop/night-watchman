package com.mnclimbingcoop.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

import org.joda.time.LocalDateTime

@CompileStatic
@ToString
class AccessHolder {

    // Cardholder Info
    String firstName
    String middleInitial
    String lastName
    String custom1
    String custom2
    String emailAddress
    String phoneNumber

    // Card Info
    List<AccessCard> cards = []

    // Access Expiration Date
    LocalDateTime endTime

}
