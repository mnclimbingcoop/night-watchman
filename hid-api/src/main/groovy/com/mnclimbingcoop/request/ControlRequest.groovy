package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.ControlVariables
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

@JacksonXmlRootElement(localName='VertXMessage')
class ControlRequest extends VertXRequest {

    ControlRequest() {
        controlVariables = new ControlVariables(action: Action.COMMAND_MODE, index: 101, value: false)
    }

    ControlRequest stopAlarm() {
        controlVariables = new ControlVariables(action: Action.COMMAND_MODE, index: 101, value: false)
        return this
    }

}
