package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Schedule
import com.mnclimbingcoop.domain.Schedules
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

class ScheduleRequest extends VertXRequest {

    ScheduleRequest() {
        schedules = new Schedules(action: Action.DESCRIBE_RECORDS)
    }

    ScheduleRequest overview() {
        schedules = new Schedules(action: Action.DESCRIBE_RECORDS)
        return this
    }

    ScheduleRequest list(Integer offset = 0,
                         Integer count = 10) {
        schedules = new Schedules(
            action: Action.LIST_RECORDS,
            recordOffset: offset,
            recordCount: count
        )
        return this
    }

    ScheduleRequest listFull() {
        schedules = new Schedules(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.CUSTOMIZED)
        return this
    }

    ScheduleRequest show(Integer scheduleID, ResponseFormat responseFormat = null) {
        schedules = new Schedules(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            scheduleID: scheduleID
        )
        return this
    }

    ScheduleRequest create(Schedule schedule) {
        schedules = new Schedules(action: Action.ADD_DATA, schedule: schedule)
        return this
    }

    ScheduleRequest update(Schedule schedule) {
        schedules = new Schedules(
            action: Action.UPDATE_DATA,
            scheduleID: schedule.scheduleID,
            schedule: schedule
        )
        return this
    }

    ScheduleRequest delete(String scheduleID) {
        schedules = new Schedules(action: Action.DELETE_DATA, scheduleID: scheduleID)
        return this
    }

}
