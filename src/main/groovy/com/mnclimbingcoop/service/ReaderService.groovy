package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Readers
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.ReaderRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class ReaderService {

    protected final HidService hidService

    @Inject
    ReaderService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, Readers> list() {
        VertXRequest request = new ReaderRequest().list()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.readers) {
                hidService.hidStates[name].readers.addAll(resp.readers.readers)
            }
            return [ name, resp.readers ]
        }
    }

    Readers list(String name) {
        VertXRequest request = new ReaderRequest().list()
        Readers readers = hidService.get(name, request)?.readers
        if (readers) {
            hidService.hidStates[name].readers.addAll(readers.readers)
        }
        return readers
    }

}
