package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.CardFormats
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

class CardFormatRequest extends VertXRequest {

    CardFormatRequest() {
        cardFormats = new CardFormats(action: Action.LIST_RECORDS, responseFormat: ResponseFormat.EXPANDED)
    }

}
