package com.mnclimbingcoop

import com.mnclimbingcoop.service.*

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class Surveyor {

    protected final AlertService alertService
    protected final CardFormatService cardFormatService
    protected final CardholderSurveyService cardholderSurveyService
    protected final CredentialSurveyService credentialSurveyService
    protected final DoorService doorService
    protected final HidService hidService
    protected final ParameterService parameterService
    protected final ReaderService readerService
    protected final ScheduleService scheduleService
    protected final SystemService systemService

    static final int MAX_TRIES = 5

    @Inject
    Surveyor(AlertService alertService,
             CardFormatService cardFormatService,
             CardholderSurveyService cardholderSurveyService,
             CredentialSurveyService credentialSurveyService,
             DoorService doorService,
             HidService hidService,
             ParameterService parameterService,
             ReaderService readerService,
             ScheduleService scheduleService,
             SystemService systemService) {

        this.alertService = alertService
        this.cardFormatService = cardFormatService
        this.cardholderSurveyService = cardholderSurveyService
        this.credentialSurveyService = credentialSurveyService
        this.doorService = doorService
        this.hidService = hidService
        this.parameterService = parameterService
        this.readerService = readerService
        this.scheduleService = scheduleService
        this.systemService = systemService

    }

    @Scheduled(fixedDelayString = '${schedule.survey.rate}', initialDelayString = '${schedule.survey.initialDelay}')
    void periodicSurvey() {
        survey()
    }

    @Async
    void survey() {

        // Really only update these once...
        safeRetry("discovering alert info") {
            alertService.list()
        }
        safeRetry("discovering card format info") {
            cardFormatService.list()
        }
        safeRetry("discovering parameter info") {
            parameterService.get()
        }

        safeRetry("discovering door info") {
            doorService.list()
        }
        safeRetry("discovering reader info") {
            readerService.list()
        }
        safeRetry("discovering schedule info") {
            scheduleService.list()
        }
        safeRetry("discovering system info") {
            systemService.get()
        }

        // Send state to SQS
        hidService.sync()

        // Update user inventory.  Once? Daily?
        log.info("discovering cardholder info")
        cardholderSurveyService.survey()

        // TODO Build from credentials remaining
        log.info("discovering credential info")
        credentialSurveyService.survey()

    }

    void safeRetry(String reason, Closure cls) {
        int tries = 0
        boolean success = false
        while (tries < MAX_TRIES && !success) {
            try {
                log.info reason
                cls()
                success = true
            } catch (HidRemoteErrorException ex) {
                tries++
                log.error "Attempt #${tries} failed while ${reason}, retrying in 3s"
                Thread.sleep(500 * tries)
            }
        }
        if (tries == MAX_TRIES) {
            log.error "Gave up.  Max tries reached while ${reason}"
        }

    }

}
