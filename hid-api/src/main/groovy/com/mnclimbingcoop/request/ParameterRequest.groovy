package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.EdgeSoloParameters
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action

import groovy.transform.CompileStatic

@CompileStatic
class ParameterRequest extends VertXRequest {

    ParameterRequest() {
        parameters = new EdgeSoloParameters(action: Action.DESCRIBE_RECORDS)
    }

    ParameterRequest get() {
        parameters = new EdgeSoloParameters(action: Action.DESCRIBE_RECORDS)
        return this
    }

}
