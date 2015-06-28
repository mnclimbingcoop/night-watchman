package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Cardholders
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.CardholderRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CardholderService {

    protected final HidService hidService

    @Inject
    CardholderService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, Cardholders> getMetaData() {
        VertXRequest request = new CardholderRequest().overview()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            return [ name, resp.cardholders ]
        }
    }

    Cardholders getMetaData(String name) {
        VertXRequest request = new CardholderRequest().overview()
        return hidService.get(name, request)?.cardholders

    }

    Cardholders listCardholders(String name, Integer offset, Integer count) {
        VertXRequest request = new CardholderRequest().list(offset, count)
        return hidService.get(name, request)?.cardholders
    }

    Cardholder getCardholder(String name, Integer cardholderID) {
        VertXRequest request = new CardholderRequest().show(cardholderID)
        return hidService.get(name, request)?.cardholders?.cardholder
    }

}
