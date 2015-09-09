package com.mnclimbingcoop.domain

import java.util.concurrent.ConcurrentSkipListSet

import java.util.regex.Pattern

class EdgeSoloState {

    String doorName

    // Inventory
    Set<AlertAction> alerts = new ConcurrentSkipListSet<AlertAction>()
    Set<CardFormat> cardFormats = new ConcurrentSkipListSet<CardFormat>()
    Set<Cardholder> cardholders = new ConcurrentSkipListSet<Cardholder>()
    Set<Credential> credentials = new ConcurrentSkipListSet<Credential>()
    EdgeSoloParameters parameters
    Set<Reader> readers = new ConcurrentSkipListSet<Reader>()
    Set<Schedule> schedules = new ConcurrentSkipListSet<Schedule>()
    System system

    // Door State
    Set<Door> doors = new ConcurrentSkipListSet<Door>()

    // Event Audits
    EventMessages eventOverview
    Set<EventMessage> events = new ConcurrentSkipListSet<EventMessage>()


    Set<Cardholder> findCardholders(String q) {
        return findCardholders(Pattern.compile(q.toLowerCase()))
    }

    Set<Cardholder> findCardholders(Pattern q) {
        return cardholders.findAll{ Cardholder cardholder ->
            boolean match = false

            cardholder.with{
                if (
                    (surname  && q.matcher(surname.toLowerCase()).find())  ||
                    (forename && q.matcher(forename.toLowerCase()).find()) ||
                    (email    && q.matcher(email.toLowerCase()).find())    ||
                    (phone    && q.matcher(phone.toLowerCase()).find())    ||
                    (custom1  && q.matcher(custom1.toLowerCase()).find())  ||
                    (custom2  && q.matcher(custom2.toLowerCase()).find())
                ) {
                    match = true
                }
            }

            return match || cardholder.credentials?.any{ Credential cred ->
                if (
                    (cred.rawCardNumber && q.matcher(cred.rawCardNumber.toLowerCase()).matches()) ||
                    (cred.cardNumber && q.matcher(cred.cardNumber).matches())
                ) {
                    return true
                }
            }

        }
    }

}
