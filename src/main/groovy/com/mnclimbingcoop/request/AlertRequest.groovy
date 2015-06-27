package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.AlertActions
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

class AlertRequest extends VertXRequest {

    AlertRequest() {
        alerts = new AlertActions(action: Action.LIST_RECORDS)
    }

}
