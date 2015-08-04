package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.ControlVariables
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

class ControlRequest extends VertXRequest {

    ControlRequest() {
        controlVariables = new ControlVariables(action: Action.COMMAND_MODE, index: 101, value: false)
    }

    ControlRequest stopAlarm() {
        controlVariables = new ControlVariables(action: Action.COMMAND_MODE, index: 101, value: false)
        return this
    }

}
