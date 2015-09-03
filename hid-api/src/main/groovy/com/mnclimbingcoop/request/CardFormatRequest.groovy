package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.CardFormats
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

@JacksonXmlRootElement(localName='VertXMessage')
class CardFormatRequest extends VertXRequest implements SimpleEntityRequest<CardFormatRequest> {

    CardFormatRequest() {
        cardFormats = new CardFormats(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.EXPANDED)
    }

    @Override
    CardFormatRequest list() {
        cardFormats = new CardFormats(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.EXPANDED)
        return this
    }

}
