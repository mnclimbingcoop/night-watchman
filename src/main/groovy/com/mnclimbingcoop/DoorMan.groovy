package com.mnclimbingcoop

import com.mnclimbingcoop.service.*

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class DoorMan {

    protected final AlertService alertService
    protected final CardFormatService cardFormatService
    protected final CardholderSurveyService cardholderSurveyService
    protected final CredentialSurveyService credentialSurveyService
    protected final DoorService doorService
    protected final ParameterService parameterService
    protected final ReaderService readerService
    protected final ScheduleService scheduleService
    protected final SystemService systemService


    @Inject
    DoorMan(AlertService alertService,
            CardFormatService cardFormatService,
            CardholderSurveyService cardholderSurveyService,
            CredentialSurveyService credentialSurveyService,
            DoorService doorService,
            ParameterService parameterService,
            ReaderService readerService,
            ScheduleService scheduleService,
            SystemService systemService) {

        this.alertService = alertService
        this.cardFormatService = cardFormatService
        this.cardholderSurveyService = cardholderSurveyService
        this.credentialSurveyService = credentialSurveyService
        this.doorService = doorService
        this.parameterService = parameterService
        this.readerService = readerService
        this.scheduleService = scheduleService
        this.systemService = systemService

    }

    /** Check for events every 3 seconds */
    @Scheduled(fixedDelay = 3000l)
    void monitor() {
        // This should probably be checked more often (every 3 sec?)
        // eventService.list()
        // Send via SQS
    }

    /** Check the doors every 20 seconds */
    @Scheduled(fixedDelay = 20000l)
    void secure() {
        // This should probably be checked more often (every 20 sec?)
        doorService.list()
        // listen via SQS
    }

    /** Wait 10 minutes before each execution */
    @Scheduled(fixedDelay = 600000l)
    void survey() {
        // Really only update these once...
        alertService.list()
        cardFormatService.list()
        parameterService.get()
        readerService.list()
        scheduleService.list()
        systemService.get()

        // Update user inventory.  Once? Daily?
        cardholderSurveyService.survey()
        credentialSurveyService.survey()
    }


}
