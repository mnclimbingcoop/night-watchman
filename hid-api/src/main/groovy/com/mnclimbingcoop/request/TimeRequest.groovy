package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Time
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

import org.joda.time.DateTime

class TimeRequest extends VertXRequest {

    // Default to central standard time
    static final String TZ = 'CST6CDT,M3.2.0/2,M11.1.0/2'
    static final String TZ_CODE = '062'

    TimeRequest() {
        time = new Time(action: Action.DESCRIBE_RECORDS)
    }

    TimeRequest get() {
        time = new Time(action: Action.DESCRIBE_RECORDS)
        return this
    }

    TimeRequest set() {
        DateTime now = DateTime.now()
        time = new Time(
            action: Action.UPDATE_DATA,
            month: now.monthOfYear,
            dayOfMonth: now.dayOfMonth,
            year: now.year,
            hour: now.hourOfDay,
            minute: now.minuteOfHour,
            second: now.secondOfMinute,
            tz: TZ,
            tzCode: TZ_CODE
        )
        return this
    }

}
