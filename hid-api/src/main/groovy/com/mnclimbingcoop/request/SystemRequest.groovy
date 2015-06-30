package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.System
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

class SystemRequest extends VertXRequest {

    SystemRequest() {
        system = new System(action: Action.DESCRIBE_RECORDS)
    }

    SystemRequest get() {
        system = new System(action: Action.DESCRIBE_RECORDS)
        return this
    }

}
