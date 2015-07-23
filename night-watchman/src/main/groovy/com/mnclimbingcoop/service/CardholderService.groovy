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

    Map<String, Cardholders> overview() {
        VertXRequest request = new CardholderRequest().overview()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            return [ name, resp.cardholders ]
        }
    }

    Cardholders overview(String name) {
        VertXRequest request = new CardholderRequest().overview()
        return hidService.get(name, request)?.cardholders

    }

    Cardholders list(String name, Integer offset, Integer count) {
        VertXRequest request = new CardholderRequest().list(offset, count)
        Cardholders cardholders = hidService.get(name, request)?.cardholders
        if (cardholders?.cardholders) {
            hidService.hidStates[name].cardholders.addAll(cardholders.cardholders)
        }
        return cardholders
    }

    Cardholder show(String name, Integer id) {
        VertXRequest request = new CardholderRequest().show(id)
        Cardholder cardholder =  hidService.get(name, request)?.cardholders?.cardholder
        if (cardholder) {
            hidService.hidStates[name].cardholders << cardholder
        }
        return cardholder
    }

    void sync() {
        hidService.sync()
    }

}
