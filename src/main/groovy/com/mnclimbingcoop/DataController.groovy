package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.service.DoorSurveyService

import javax.inject.Inject

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class DataController {

    protected final DoorSurveyService doorSurveyService

    @Inject
    DataController(DoorSurveyService doorSurveyService) {
        this.doorSurveyService = doorSurveyService
    }

    @RequestMapping(value = '/cardholders', method = RequestMethod.GET, produces = 'application/json')
    Map<String, List<Cardholder>> getCardholders() {
        return doorSurveyService.cardholderMap
    }

}

