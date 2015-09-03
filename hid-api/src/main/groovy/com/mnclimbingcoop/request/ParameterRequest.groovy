package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.EdgeSoloParameters
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

@JacksonXmlRootElement(localName='VertXMessage')
class ParameterRequest extends VertXRequest {

    ParameterRequest() {
        parameters = new EdgeSoloParameters(action: Action.DESCRIBE_RECORDS)
    }

    ParameterRequest get() {
        parameters = new EdgeSoloParameters(action: Action.DESCRIBE_RECORDS)
        return this
    }

}
