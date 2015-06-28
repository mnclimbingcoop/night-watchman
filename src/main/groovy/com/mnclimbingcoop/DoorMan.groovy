package com.mnclimbingcoop

import com.mnclimbingcoop.domain.Door
import com.mnclimbingcoop.service.DoorService
import com.mnclimbingcoop.service.DoorSurveyService

import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@Named
@Slf4j
class DoorMan {

    protected final DoorService doorService
    protected final DoorSurveyService doorSurveyService

    @Inject
    DoorMan(DoorService doorService, DoorSurveyService doorSurveyService) {
        this.doorService = doorService
        this.doorSurveyService = doorSurveyService
    }

    @Scheduled(fixedRate = 300000l)
    void survey() {
        doorSurveyService.survey()
    }

}
