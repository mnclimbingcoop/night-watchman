package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.Reports
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ReportType

@JacksonXmlRootElement(localName='VertXMessage')
class ReportRequest extends VertXRequest {

    ReportRequest() {
        reports = new Reports(action: Action.COMMAND_MODE, type: ReportType.EVENTS)
    }

    ReportRequest events() {
        reports = new Reports(action: Action.COMMAND_MODE, type: ReportType.EVENTS)
        return this
    }

}
