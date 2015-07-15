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
class CardholderSurveyService {

    static final Integer PAGE_SIZE = 10
    protected final CardholderService service

    @Inject
    CardholderSurveyService(CardholderService cardholderService) {
        this.service = cardholderService
    }

    void survey() {
        // Loop through all card members for each door
        counts.each{ String doorName, Integer inUse ->
            log.info "Found ${inUse} card holders for ${doorName}"
            Integer offset = 0
            Integer added = 0
            // initialize list
            while (offset <= inUse) {
                Cardholders cardholders = service.list(doorName, offset, PAGE_SIZE)
                cardholders.cardholders.each{ Cardholder cardholder ->
                    log.debug " + adding [#${cardholder.cardholderID}]: ${cardholder.forename} ${cardholder.surname}"
                    added++
                }
                offset += PAGE_SIZE
                log.info " ? retrieved ${offset} out of ${inUse} card holders for door ${doorName}."
            }
            log.info "Done retrieving ${added} cardholders for ${doorName} door."
        }
    }

    protected Map<String, Integer> getCounts() {
        Map<String, Integer> counts = [:]
        Map<String, Cardholders> metaData = service.overview()
        metaData.each{ String doorName, Cardholders cardholders ->
            counts[doorName] = cardholders.inUse
        }
        return counts
    }

}
