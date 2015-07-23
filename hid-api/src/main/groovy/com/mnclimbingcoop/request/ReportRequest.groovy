package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ReportType

import groovy.transform.CompileStatic

@CompileStatic
class ReportRequest extends VertXRequest {

    ReportRequest() {
        reports = new Reports(action: Action.COMMAND_MODE, type: ReportType.EVENTS)
    }

    ReportRequest events() {
        reports = new Reports(action: Action.COMMAND_MODE, type: ReportType.EVENTS)
        return this
    }

}
