package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Credentials

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CredentialSurveyService {

    static final Integer PAGE_SIZE = 10
    protected final CredentialService service

    @Inject
    CredentialSurveyService(CredentialService credentialService) {
        this.service = credentialService
    }

    void survey() {
        // Loop through all card members for each door
        counts.each{ String doorName, Integer inUse ->
            log.info "Found ${inUse} credentials for ${doorName}"
            Integer offset = 0
            Integer added = 0
            // initialize list
            while (offset <= inUse) {
                Credentials credentials = service.list(doorName, offset, PAGE_SIZE)
                credentials?.credentials?.each{ Credential credential ->
                    log.debug " + adding [#${credential.rawCardNumber}]: ${credential.cardNumber} ${credential.formatName}, owner: ${credential.cardholderID}"
                    added++
                }
                offset += PAGE_SIZE
                log.info " ? retrieved ${offset} out of ${inUse} credentials for door ${doorName}."
            }
            log.info "Done retrieving ${added} credentials for ${doorName} door."
        }
        service.sync()
    }

    protected Map<String, Integer> getCounts() {
        Map<String, Integer> counts = [:]
        Map<String, Credentials> metaData = service.overview()
        metaData.each{ String doorName, Credentials credentials ->
            counts[doorName] = credentials.inUse
        }
        return counts
    }

}
