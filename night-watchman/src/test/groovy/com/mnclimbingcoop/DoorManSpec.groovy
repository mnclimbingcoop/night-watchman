package com.mnclimbingcoop

import com.mnclimbingcoop.service.*
import spock.lang.Specification

class DoorManSpec extends Specification {

    DoorMan doorMan

    protected AlertService alertService
    protected CardFormatService cardFormatService
    protected CardholderSurveyService cardholderSurveyService
    protected CredentialSurveyService credentialSurveyService
    protected DoorService doorService
    protected ParameterService parameterService
    protected ReaderService readerService
    protected ScheduleService scheduleService
    protected SystemService systemService

    void setup() {
        alertService = Mock()
        cardFormatService = Mock()
        cardholderSurveyService = Mock()
        credentialSurveyService = Mock()
        doorService = Mock()
        parameterService = Mock()
        readerService = Mock()
        scheduleService = Mock()
        systemService = Mock()

        doorMan = new DoorMan(alertService,
                              cardFormatService,
                              cardholderSurveyService,
                              credentialSurveyService,
                              doorService,
                              parameterService,
                              readerService,
                              scheduleService,
                              systemService)
    }

    void 'secure checks doors'() {
        when:
        doorMan.secure()

        then:
        1 * doorService.list()
        0 * _
    }

    void 'survey gathers door data'() {
        when:
        doorMan.survey()

        then:
        1 * alertService.list()
        1 * cardFormatService.list()
        1 * parameterService.get()
        1 * readerService.list()
        1 * scheduleService.list()
        1 * systemService.get()
        1 * cardholderSurveyService.survey()
        1 * credentialSurveyService.survey()
        0 * _
    }

}
