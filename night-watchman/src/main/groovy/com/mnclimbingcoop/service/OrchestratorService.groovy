package com.mnclimbingcoop.service

import com.mnclimbingcoop.Surveyor
import com.mnclimbingcoop.domain.AccessCard
import com.mnclimbingcoop.domain.AccessHolder
import com.mnclimbingcoop.domain.CardFormat
import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.Meta
import com.mnclimbingcoop.domain.Role
import com.mnclimbingcoop.domain.RoleSet
import com.mnclimbingcoop.domain.Roles
import com.mnclimbingcoop.domain.Schedule
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.request.CardholderRequest
import com.mnclimbingcoop.request.CredentialRequest
import com.mnclimbingcoop.wiegand.WiegandEncoder

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.joda.time.LocalDateTime

@Named
@Slf4j
class OrchestratorService {

    protected final HidService hidService
    protected final Surveyor surveyor

    @Inject
    OrchestratorService(HidService hidService, Surveyor surveyor) {
        this.hidService = hidService
        this.surveyor = surveyor
    }

    void orchestrate(VertXRequest request) {
        String door = request.doorName
        Meta meta = request.meta

        // Currently will only orchestrate adding/updating an access holder
        if (meta.refresh) { refresh() }
        if (meta.accessHolder) { orchestrate(door, meta.accessHolder) }
    }

    void refresh() {
        log.info 'refreshing all door data'
        surveyor.survey()
    }

    void orchestrate(String door, AccessHolder accessHolder) {
        // Look for cardholder
        EdgeSoloState state = hidService.hidStates[door]

        if (state) {
            // Find or create card holder
            Cardholder cardholder = findCardholder(state, accessHolder)
            if (!cardholder) {
                cardholder = createCardholder(door, accessHolder)
            } else {
                cardholder = updateCardholder(door, accessHolder, cardholder)
            }

            // Update cardholder info if changes detected
            if (cardholder) {
                // Assign/create/update credentials
                assignCredentials(door, state, accessHolder, cardholder)
                // Assign/update schedule
                assignSchedule(door, state, cardholder)
            }

        } else {
            log.error "Door '${door}' not found."
        }
    }

    protected void assignCredentials(String door,
                                     EdgeSoloState state,
                                     AccessHolder accessHolder,
                                     Cardholder cardholder) {
        accessHolder.cards.each{ AccessCard card ->
            // Get card format id
            Integer formatID = null
            if (card.formatName && card.cardNumber) {
                formatID = state.cardFormats.find{ it.formatName.equalsIgnoreCase(card.formatName) }?.formatID
                if (!formatID) {
                    log.error "Unable to find formatID for card format '${card.formatName}'"
                }
            }

            Credential credential

            // Find card if existing
            if (!card.rawCardNumber && !formatID && !card.cardNumber) {
                log.error 'Card does not have a raw number or card number with format ID. {}', card
            } else {
                // Find or create credential
                if (cardholder.credentials) {
                    credential = findCredential(card, formatID, cardholder.credentials.toList())
                }
                if (!credential) { credential = findCredential(card, formatID, state.credentials.toList()) }
                if (!credential) {
                    List<Credential> creds = (List<Credential>) (state.cardholders*.credentials)?.flatten() ?: []
                    credential = findCredential(card, formatID, creds.toList())
                }
                if (!credential) { credential = createCredential(door, card, accessHolder.endTime, formatID) }
            }

            if (credential) {
                // Update expiration if needed
                updateCredential(door, card, formatID, accessHolder.endTime, credential)
                // Assign credential or update expiration
                assignCredential(door, credential, accessHolder.endTime, cardholder)

            }

        }
    }

    protected void assignCredential(String door, Credential credential, LocalDateTime endTime, Cardholder cardholder) {
        if (!(credential.rawCardNumber in cardholder.credentials*.rawCardNumber)) {
            log.info "Assigning card ${credential.rawCardNumber} to card holder ${cardholder.cardholderID}"
            VertXRequest request = new CredentialRequest()
                    .updateCardholder(credential.rawCardNumber, cardholder.cardholderID)
                    .forDoor(door)
            hidService.get(door, request)
        }

        if (credential.endTime != endTime) {
            log.info "Updating expiration ${endTime} to card card ${credential.rawCardNumber}"
            VertXRequest request = new CredentialRequest()
                    .updateExpiration(credential.rawCardNumber, endTime)
                    .forDoor(door)
            hidService.get(door, request)
        }

        // Update State
        credential.cardholderID = cardholder.cardholderID
        if (!cardholder.credentials) { cardholder.credentials = [] }
        cardholder.credentials.remove(credential)
        cardholder.credentials.add(credential)
        hidService.hidStates[door].cardholders.remove(cardholder)
        hidService.hidStates[door].cardholders.add(cardholder)

    }

    protected Credential findCredential(AccessCard card, Integer formatID, List<Credential> credentials) {
        if (credentials && card) {
            // Find or create credential
            Collection<Credential> creds = credentials.findAll{ Credential cred ->
                if (cred) {
                    if (formatID && card.cardNumber) {
                        return (card.cardNumber == cred.cardNumber && formatID == cred.formatID)
                    } else if (card.rawCardNumber) {
                        return card.rawCardNumber?.equalsIgnoreCase(cred.rawCardNumber)
                    }
                }
            } as List

            if (creds.size() == 1) {
                return creds[0]
            } else if (creds.size() > 1) {
                log.error 'Multiple credentials matched! {}, using first one.', creds
                return creds[0]
            }
        }
    }

    Integer getFacilityCode(String door, Integer formatID) {
        CardFormat cardFormat = hidService.hidStates[door].cardFormats.find{ CardFormat format ->
            format.formatID == formatID
        }
        if (cardFormat.cardFormats) {
            return cardFormat.cardFormats[0].value
        }
        log.warn "Unable to find facility number for card format for formatID ${formatID}"
    }

    protected Credential createCredential(String door, AccessCard card, LocalDateTime endTime, Integer formatID) {
        log.info 'Creating credentials: {}', card
        Credential credential = new Credential(
            cardNumber:    card.cardNumber,
            endTime:       endTime,
            formatID:      formatID,
            isCard:        true,
            rawCardNumber: card.rawCardNumber
        )
        VertXRequest request = new CredentialRequest().create(credential).forDoor(door)
        try {
            VertXResponse response = hidService.get(door, request)
            if (response?.credentials?.credentials) {
                credential = response.credentials.credentials[0]

                // propogate rawCardNumber if available
                if (!credential.rawCardNumber) {
                    Integer facilityCode = getFacilityCode(door, formatID)
                    if (facilityCode) {
                        Integer cardNumber = credential.cardNumber as Integer
                        credential.rawCardNumber = WiegandEncoder.encode(facilityCode, cardNumber)
                    }
                }

                // Add credential to state
                hidService.hidStates[door].credentials.remove(credential)
                hidService.hidStates[door].credentials.add(credential)
                return credential
            }
        } catch (HidRemoteErrorException ex) {
            if (ex.error.errorCode == '19' && ex.error.elementType == 'hid:Credentials') {
                log.info "Create credential failed because it already exists?: ${ex.message}"
            } else { throw ex }
        }
        return null
    }

    protected Credential updateCredential(String door,
                                          AccessCard card,
                                          Integer formatID,
                                          LocalDateTime endTime,
                                          Credential credential) {

        // Set rawCardNumber if available
        if (!card.rawCardNumber && credential.rawCardNumber) {
            card.rawCardNumber = credential.rawCardNumber
        }
        if (endTime == credential.endTime) { return }

        log.info 'Updating credentials: {}', card
        Credential cred = new Credential(
            cardNumber:    card.cardNumber,
            endTime:       endTime,
            formatID:      formatID,
            isCard:        true,
            rawCardNumber: card.rawCardNumber
        )

        VertXRequest request = new CredentialRequest().update(cred).forDoor(door)
        VertXResponse response = hidService.get(door, request)
        if (response?.credentials?.credentials) {
            credential = response.credentials.credentials[0]
            // Add credential to state
        }

        credential.endTime = endTime
        hidService.hidStates[door].credentials.remove(credential)
        hidService.hidStates[door].credentials.add(credential)

        return credential
    }


    protected void assignSchedule(String door, EdgeSoloState state, Cardholder cardholder) {

        Schedule schedule = state.schedules.find{ it.scheduleName == '24x7' }
        if (!schedule) {
            log.error 'unable to find 24x7 schedule!'
            return
        }

        Set<Integer> scheduleIDs = (cardholder.roles*.scheduleID)?.flatten() as Set
        if (schedule.scheduleID in scheduleIDs) { return }

        Role role = new Role(roleID: cardholder.cardholderID, scheduleID: schedule.scheduleID, resourceID: 0)

        VertXRequest request = new VertXRequest().forDoor(door)
        request.roleSet = new RoleSet(
            action: Action.UPDATE_DATA,
            roleSetID: cardholder.roleSetID,
            roles: new Roles(roles:[ role ])
        )

        log.info 'Assigning 24x7 scheduleID: {} to cardholder {}', schedule.scheduleID, cardholder.cardholderID
        hidService.get(door, request)
    }

    protected Cardholder createCardholder(String door, AccessHolder accessHolder) {
        Cardholder ch = fromAccessHolder(accessHolder)
        log.info 'Creating cardholder: {}', ch
        VertXRequest request = new CardholderRequest().create(ch)
        VertXResponse response = hidService.get(door, request)
        if (response?.cardholders?.cardholders) {
            Cardholder cardholder = response.cardholders.cardholders[0]
            // Update State
            hidService.hidStates[door].cardholders.remove(cardholder)
            hidService.hidStates[door].cardholders.add(cardholder)
            return cardholder
        }
        return null
    }

    protected AccessHolder cleanAccessHolder(AccessHolder accessHolder) {
        if (accessHolder.firstName ==~ /\s*/ )     { accessHolder.firstName = null }
        if (accessHolder.middleInitial ==~ /\s*/ ) { accessHolder.middleInitial = null }
        if (accessHolder.lastName ==~ /\s*/ )      { accessHolder.lastName = null }
        if (accessHolder.emailAddress ==~ /\s*/ )  { accessHolder.emailAddress = null }
        if (accessHolder.phoneNumber ==~ /\s*/ )   { accessHolder.phoneNumber = null }
        if (accessHolder.custom1 ==~ /\s*/ )       { accessHolder.custom1 = null }
        if (accessHolder.custom2 ==~ /\s*/ )       { accessHolder.custom2 = null }
        return accessHolder
    }

    /*
    <?xml version="1.0" encoding="UTF-8"?>
    <VertXMessage>
      <hid:Credentials action="UD"  rawCardNumber="03644E22"  isCard="true">
        <hid:Credential  endTime="2015-09-22T23:59:59"/>
      </hid:Credentials>
    </VertXMessage>
    */

    protected Cardholder updateCardholder(String door, AccessHolder accessHolder, Cardholder cardholder) {

        accessHolder = cleanAccessHolder(accessHolder)

        if (
            cardholder.forename   == accessHolder.firstName &&
            cardholder.middleName == accessHolder.middleInitial &&
            cardholder.surname    == accessHolder.lastName &&
            cardholder.email      == accessHolder.emailAddress &&
            cardholder.phone      == accessHolder.phoneNumber &&
            cardholder.custom1    == accessHolder.custom1 &&
            cardholder.custom2    == accessHolder.custom2
        ) {
            return cardholder
        }

        log.info "Cardholder diff:"
        log.info "${cardholder.forename}   vs. ${accessHolder.firstName}"
        log.info "${cardholder.middleName} vs. ${accessHolder.middleInitial}"
        log.info "${cardholder.surname}    vs. ${accessHolder.lastName}"
        log.info "${cardholder.email}      vs. ${accessHolder.emailAddress}"
        log.info "${cardholder.phone}      vs. ${accessHolder.phoneNumber}"
        log.info "${cardholder.custom1}    vs. ${accessHolder.custom1}"
        log.info "${cardholder.custom2}    vs. ${accessHolder.custom2}"

        Cardholder ch = fromAccessHolder(accessHolder)
        ch.cardholderID = cardholder.cardholderID

        log.info 'Updating cardholder: {}', ch
        VertXRequest request = new CardholderRequest().update(ch)
        try {
            VertXResponse response = hidService.get(door, request) // <<<<
            if (response?.cardholders?.cardholders) {
                cardholder = response.cardholders.cardholders[0]
            }

            cardholder.forename   = accessHolder.firstName
            cardholder.middleName = accessHolder.middleInitial
            cardholder.surname    = accessHolder.lastName
            cardholder.email      = accessHolder.emailAddress
            cardholder.phone      = accessHolder.phoneNumber
            cardholder.custom1    = accessHolder.custom1
            cardholder.custom2    = accessHolder.custom2

            // Update State
            hidService.hidStates[door].cardholders.remove(cardholder)
            hidService.hidStates[door].cardholders.add(cardholder)

        } catch (HidRemoteErrorException ex) {
            if (ex.error.errorCode == '19' && ex.error.elementType == 'hid:Cardholders') {
                log.info "Update cardholder failed: ${ex.message}"
            } else { throw ex }
        }

        return cardholder
    }

    protected Cardholder findCardholder(EdgeSoloState state, AccessHolder accessHolder) {
        // First try and find by access card
        Cardholder cardholder = state.cardholders.find{ Cardholder ch ->
            Set<String> rawNumbers = accessHolder.cards*.rawCardNumber as Set
            Set<String> cardNumbers = accessHolder.cards.collect{ "${it.formatName}:${it.cardNumber}" } as Set
            cardNumbers.remove(':')
            cardNumbers.remove('null:null')
            ch.credentials?.any{ Credential cred ->
                if (cred.formatName && cred.cardNumber) {
                    return "${cred.formatName}:${cred.cardNumber}" in cardNumbers
                } else {
                    return cred.rawCardNumber in rawNumbers
                }
            }
        }

        if (!cardholder) {
            // Next try finding by name
            Collection<Cardholder> cardholders = state.cardholders.findAll{ Cardholder ch ->
                ch.forename?.equalsIgnoreCase(accessHolder.firstName) &&
                ch.surname?.equalsIgnoreCase(accessHolder.lastName)
            }
            if (cardholders.size() == 1) {
                cardholder = cardholders[0]
            } else if (cardholders.size() > 1) {
                log.error("Too many card holders found. {}", cardholders)
            }
        }
        return cardholder
    }

    protected Cardholder fromAccessHolder(AccessHolder accessHolder) {
        return new Cardholder(
            forename:           accessHolder.firstName,
            middleName:         accessHolder.middleInitial,
            surname:            accessHolder.lastName,
            email:              accessHolder.emailAddress,
            phone:              accessHolder.phoneNumber,
            custom1:            accessHolder.custom1,
            custom2:            accessHolder.custom2,
            confirmingPin:      '',
            extendedAccess:     false,
            exemptFromPassback: true
        )
    }

}
