package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ReportType

class ReportRequest extends VertXRequest {

    static final Integer readerID = 1

    ReportRequest() {
        reports = new Reports(action: Action.COMMAND_MODE, type: ReportType.EVENTS)
    }

    ReportRequest events() {
        reports = new Reports(action: Action.COMMAND_MODE, type: ReportType.EVENTS)
        return this
    }

}
