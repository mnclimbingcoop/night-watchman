package com.mnclimbingcoop

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CredentialService {

    protected final HidService hidService

    @Inject
    CredentialService(HidService hidService) {
        this.hidService = hidService
    }

    void buildCredentialDatabase() {
    }

    void buildScheduleDatabase() {
    }

}

