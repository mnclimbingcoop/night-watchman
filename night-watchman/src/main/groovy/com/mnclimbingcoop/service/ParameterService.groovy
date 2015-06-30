package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.EdgeSoloParameters
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.ParameterRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class ParameterService {

    protected final HidService hidService

    @Inject
    ParameterService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, EdgeSoloParameters> get() {
        VertXRequest request = new ParameterRequest().get()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.parameters) {
                hidService.hidStates[name].parameters = resp.parameters
            }
            return [ name, resp.parameters ]
        }
    }

    EdgeSoloParameters get(String name) {
        VertXRequest request = new ParameterRequest().get()
        EdgeSoloParameters parameters = hidService.get(name, request)?.parameters
        if (parameters) {
            hidService.hidStates[name].parameters = parameters
        }
        return parameters
    }

}
