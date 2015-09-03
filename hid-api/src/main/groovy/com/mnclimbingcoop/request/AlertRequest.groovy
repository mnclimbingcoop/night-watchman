package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.AlertActions
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

@JacksonXmlRootElement(localName='VertXMessage')
class AlertRequest extends VertXRequest implements SimpleEntityRequest<AlertRequest> {

    AlertRequest() {
        alerts = new AlertActions(action: Action.LIST_RECORDS)
    }

    @Override
    AlertRequest list() {
        alerts = new AlertActions(action: Action.LIST_RECORDS)
        return this
    }

}
