package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.CardFormats
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.CardFormatRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CardFormatService {

    protected final HidService hidService

    @Inject
    CardFormatService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, CardFormats> list() {
        VertXRequest request = new CardFormatRequest().list()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            if (resp.cardFormats) {
                hidService.hidStates[name].cardFormats.addAll(resp.cardFormats.cardFormats)
            }
            return [ name, resp.cardFormats ]
        }
    }

    CardFormats list(String name) {
        VertXRequest request = new CardFormatRequest().list()
        CardFormats cardFormats = hidService.get(name, request)?.cardFormats
        if (cardFormats) {
            hidService.hidStates[name].cardFormats.addAll(cardFormats.cardFormats)
        }
        return cardFormats
    }

}
