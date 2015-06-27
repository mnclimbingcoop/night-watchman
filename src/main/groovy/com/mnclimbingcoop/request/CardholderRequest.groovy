package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Cardholders
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

class CardholderRequest extends VertXRequest {

    CardholderRequest() {
        cardholders = new Cardholders(action: Action.DESCRIBE_RECORDS)
    }

    CardholderRequest overview() {
        cardholders = new Cardholders(action: Action.DESCRIBE_RECORDS)
        return this
    }

    CardholderRequest list(Integer offset = 0,
                           Integer count = 10,
                           ResponseFormat responseFormat = ResponseFormat.EXPANDED) {
        cardholders = new Cardholders(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            recordOffset: offset,
            recordCount: count
        )
        return this
    }


    CardholderRequest show(Integer cardholderID,
                           ResponseFormat responseFormat = ResponseFormat.EXPANDED) {
        cardholders = new Cardholders(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            cardholderID: cardholderID
        )
        return this
    }

    CardholderRequest create(Cardholder cardholder) {
        cardholders = new Cardholders(action: Action.ADD_DATA, cardholder: cardholder)
        return this
    }

    CardholderRequest update(Cardholder cardholder) {
        cardholders = new Cardholders(
            action: Action.UPDATE_DATA,
            cardholderID: cardholder.cardholderID,
            cardholder: cardholder
        )
        return this
    }

    CardholderRequest delete(String cardholderID) {
        cardholders = new Cardholders(
            action: Action.DELETE_DATA,
            cardholderID: cardholderID
        )
        return this
    }

}
