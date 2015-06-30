package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.System
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
class SystemService {

    protected final HidService hidService

    @Inject
    SystemService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, System> get() {
        VertXRequest request = new ParameterRequest().get()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.system) {
                hidService.hidStates[name].system = resp.system
            }
            return [ name, resp.system ]
        }
    }

    System get(String name) {
        VertXRequest request = new ParameterRequest().get()
        System system = hidService.get(name, request)?.system
        if (system) {
            hidService.hidStates[name].system = system
        }
        return system
    }

}
