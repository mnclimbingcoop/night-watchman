package com.mnclimbingcoop.client

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import retrofit.RestAdapter

@Slf4j
@CompileStatic
class RetrofitLogger implements RestAdapter.Log {

    @Override
    void log(String message) {
        log.debug(message)
    }
}
