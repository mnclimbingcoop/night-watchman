package com.mnclimbingcoop

import com.mnclimbingcoop.service.*

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class Surveyor {

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
    Surveyor(AlertService alertService,
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

    /** Wait 12 hours before each execution */
    //@Scheduled(cron = '0 0 */12 * * *')
    void survey() {

        // Really only update these once...
        alertService.list()
        cardFormatService.list()
        parameterService.get()

        doorService.list()
        readerService.list()
        scheduleService.list()
        systemService.get()

        // Update user inventory.  Once? Daily?
        cardholderSurveyService.survey()
        credentialSurveyService.survey()

        // Send via SQS
        hidService.sync()
    }

}
