package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.AccessCard
import com.mnclimbingcoop.domain.AccessHolder
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

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.joda.time.LocalDateTime

@Named
@Slf4j
class OrchestratorService {

    protected final HidService hidService

    @Inject
    OrchestratorService(HidService hidService) {
        this.hidService = hidService
    }

    void orchestrate(VertXRequest request) {
        String door = request.doorName
        Meta meta = request.meta

        // Currently will only orchestrate adding/updating an access holder
        if (meta.accessHolder) { orchestrate door, meta.accessHolder }
    }

    void orchestrate(String door, AccessHolder accessHolder) {
        // Look for cardholder
        EdgeSoloState state = hidService.hidStates[door]

        if (state) {
            // Find or create card holder
            Cardholder cardholder = findOrCreateCardholer(door, state, accessHolder)
            // Update cardholder info if changes detected
            cardholder = updateCardholder(door, accessHolder, cardholder)
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
                credential = findCredential(card, formatID, cardholder.credentials)
                if (!credential) { credential = findCredential(card, formatID, state.credentials) }
                if (!credential) {
                    List<Credential> creds = (List<Credential>) (state.cardholders*.credentials)?.flatten() ?: []
                    credential = findCredential(card, formatID, creds)
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

    protected assignCredential(String door, Credential credential, LocalDateTime endTime, Cardholder cardholder) {
        if (!(credential.rawCardNumber in cardholder.credentials*.rawCardNumber)) {
            log.info "Assigning card ${credential.rawCardNumber} to card holder ${cardholder.cardholderID}"
            VertXRequest request = new CredentialRequest()
                    .updateCardholder(credential.rawCardNumber, cardholder.cardholderID)
                    .forDoor(door)
            hidService.get(door, request)
        }

        if (credential.endTime != endTime) {
            log.info "Assigning expiration ${endTime} to card card ${credential.rawCardNumber}"
            VertXRequest request = new CredentialRequest()
                    .updateExpiration(credential.rawCardNumber, endTime)
                    .forDoor(door)
            hidService.get(door, request)

        }

    }

    protected Credential findCredential(AccessCard card, Integer formatID, Set<Credential> credentials) {
        return findCredential(card, formatID, credentials as List)
    }

    protected Credential findCredential(AccessCard card, Integer formatID, List<Credential> credentials) {
        if (credentials) {
            // Find or create credential
            Collection<Credential> creds = credentials.findAll{ Credential cred ->
                if (formatID && card.cardNumber) {
                    return (card.cardNumber == cred.cardNumber && formatID == cred.formatID)
                } else if (card.rawCardNumber) {
                    return card.rawCardNumber.equalsIgnoreCase(cred.rawCardNumber)
                }
            }

            if (creds.size() == 1) {
                return creds[0]
            } else if (creds.size() > 1) {
                log.error 'Multiple credentials matched! {}, using first one.', creds
                return creds[0]
            }
        }
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
        VertXResponse response = hidService.get(door, request)
        if (response?.credentials?.credentials) {
            return response.credentials.credentials[0]
        }
        return null
    }

    protected Credential updateCredential(String door,
                                          AccessCard card,
                                          Integer formatID,
                                          LocalDateTime endTime,
                                          Credential credential) {
        if (endTime == credential.endTime) { return }

        log.info 'Updating credentials: {}', card
        Credential cred = new Credential(
            cardNumber:    card.cardNumber,
            endTime:       endTime,
            formatID:      formatID,
            isCard:        true,
            rawCardNumber: card.rawCardNumber
        )
        VertXRequest request = new CredentialRequest().create(cred).forDoor(door)
        VertXResponse response = hidService.get(door, request)
        if (response?.credentials?.credentials) {
            return response.credentials.credentials[0]
        }
        return null
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

    protected Cardholder findOrCreateCardholer(String door, EdgeSoloState state, AccessHolder accessHolder) {
        Cardholder cardholder = findCardholder(state, accessHolder)
        if (!cardholder) {
            // create cardholder
            cardholder = createCardholder(door, accessHolder)
        }
        return cardholder
    }

    protected Cardholder updateCardholder(String door, AccessHolder accessHolder, Cardholder cardholder) {
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
        VertXRequest request = new CardholderRequest().create(ch)
        VertXResponse response = hidService.get(door, request)
        if (response?.cardholders?.cardholders) {
            return response.cardholders.cardholders[0]
        }
        return cardholder
    }

    protected Cardholder createCardholder(String door, AccessHolder accessHolder) {
        Cardholder ch = fromAccessHolder(accessHolder)
        log.info 'Creating cardholder: {}', ch
        VertXRequest request = new CardholderRequest().create(ch)
        VertXResponse response = hidService.get(door, request)
        if (response?.cardholders?.cardholders) {
            return response.cardholders.cardholders[0]
        }
        return null
    }

    protected Cardholder findCardholder(EdgeSoloState state, AccessHolder accessHolder) {
        // First try and find by access card
        Cardholder cardholder = state.cardholders.find{ Cardholder ch ->
            Set<String> rawNumbers = accessHolder.cards*.rawCardNumber as Set
            Set<String> cardNumbers = accessHolder.cards.collect{ "${it.formatName}:${it.cardNumber}" } as Set
            cardNumbers.remove(':')
            cardNumbers.remove('null:null')
            ch.credentials.any{ Credential cred ->
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
                ch.forename.equalsIgnoreCase(accessHolder.firstName) &&
                ch.surname.equalsIgnoreCase(accessHolder.lastName)
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
