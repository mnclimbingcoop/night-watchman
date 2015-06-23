package com.mnclimbingcoop

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class UserService {

    protected final HidService hidService

    @Inject
    UserService(HidService hidService) {
        this.hidService = hidService
    }

    void buildUserDatabase() {

    }

}

