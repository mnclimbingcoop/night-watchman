package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.Schedule
import com.mnclimbingcoop.domain.Schedules
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

@JacksonXmlRootElement(localName='VertXMessage')
class ScheduleRequest extends VertXRequest implements SimpleEntityRequest<ScheduleRequest> {

    ScheduleRequest() {
        schedules = new Schedules(action: Action.DESCRIBE_RECORDS)
    }

    ScheduleRequest overview() {
        schedules = new Schedules(action: Action.DESCRIBE_RECORDS)
        return this
    }

    @Override
    ScheduleRequest list() {
        schedules = new Schedules(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.CUSTOMIZED)
        return this
    }

    ScheduleRequest list(Integer offset, Integer count) {
        schedules = new Schedules(
            action: Action.LIST_RECORDS,
            recordOffset: offset,
            recordCount: count
        )
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

    ScheduleRequest delete(Integer scheduleID) {
        schedules = new Schedules(action: Action.DELETE_DATA, scheduleID: scheduleID)
        return this
    }

}
