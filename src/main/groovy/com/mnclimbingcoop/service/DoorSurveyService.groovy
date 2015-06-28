package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Cardholders

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class DoorSurveyService {

    static final Integer PAGE_SIZE = 10
    protected final CardholderService cardholderService
    protected Map<String, Integer> cardholderCount = [:]

    // TODO Concurrent friendly
    Map<String, List<Cardholder>> cardholderMap = [:]

    @Inject
    DoorSurveyService(CardholderService cardholderService) {
        this.cardholderService = cardholderService
    }

    void survey() {
        if (!cardholderMap) {
            discoverCardholders()
        }
    }

    void discoverCardholders() {
        // get overview / metadata
        Map<String, Cardholders> metaData = cardholderService.getMetaData()
        metaData.each{ String doorName, Cardholders cardholders ->
            cardholderCount[doorName] = cardholders.inUse
        }

        // Loop through
        cardholderCount.each{ String doorName, Integer inUse ->
            log.info "Found ${inUse} card holders for ${doorName}"
            Integer offset = 0
            // initialize list
            cardholderMap[doorName] = []
            while (offset <= inUse) {
                Cardholders cardholders = cardholderService.listCardholders(doorName, offset, PAGE_SIZE)
                cardholders.cardholders.each{ Cardholder cardholder ->
                    log.info " + adding [#${cardholder.cardholderID}]: ${cardholder.forename} ${cardholder.surname}"
                    cardholderMap[doorName] << cardholder
                }
                offset += PAGE_SIZE
                log.info " ? retrieved ${offset} out of ${inUse} card holders for door ${doorName}."
            }
            log.info "Done retrieving ${cardholderMap[doorName].size()} cardholders for ${doorName} door."
        }
    }


}
