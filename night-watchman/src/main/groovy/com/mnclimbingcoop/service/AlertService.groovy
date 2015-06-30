package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.AlertAction
import com.mnclimbingcoop.domain.AlertActions
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.AlertRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class AlertService {

    protected final HidService hidService

    @Inject
    AlertService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, AlertActions> list() {
        VertXRequest request = new AlertRequest().list()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.alerts) {
                hidService.hidStates[name].alerts.addAll(resp.alerts.alertActions)
            }
            return [ name, resp.alerts ]
        }
    }

    AlertActions list(String name) {
        VertXRequest request = new AlertRequest().list()
        AlertActions alerts = hidService.get(name, request)?.alerts
        if (alerts) {
            hidService.hidStates[name].alerts.addAll(alerts.alertActions)
        }
        return alerts
    }

}
