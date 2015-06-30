package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Schedules
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.ScheduleRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class ScheduleService {

    protected final HidService hidService

    @Inject
    ScheduleService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, Schedules> list() {
        VertXRequest request = new ScheduleRequest().list()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.schedules) {
                hidService.hidStates[name].schedules.addAll(resp.schedules.schedules)
            }
            return [ name, resp.schedules ]
        }
    }

    Schedules list(String name) {
        VertXRequest request = new ScheduleRequest().list()
        Schedules schedules = hidService.get(name, request)?.schedules
        if (schedules) {
            hidService.hidStates[name].schedules.addAll(schedules.schedules)
        }
        return schedules
    }

}
