package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Cardholders
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

@JacksonXmlRootElement(localName='VertXMessage')
class CardholderRequest extends VertXRequest implements EntityCollectionRequest<CardholderRequest, Cardholder> {

    CardholderRequest() {
        cardholders = new Cardholders(action: Action.DESCRIBE_RECORDS)
    }

    @Override
    CardholderRequest overview() {
        cardholders = new Cardholders(action: Action.DESCRIBE_RECORDS)
        return this
    }

    @Override
    CardholderRequest list(Integer offset, Integer count) {
        return list(offset, count, ResponseFormat.EXPANDED)
    }

    CardholderRequest list(Integer offset,
                           Integer count,
                           ResponseFormat responseFormat) {
        cardholders = new Cardholders(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            recordOffset: offset,
            recordCount: count
        )
        return this
    }

    CardholderRequest show(Integer cardholderID) {
        return show(cardholderID, ResponseFormat.EXPANDED)
    }

    @Override
    CardholderRequest show(String cardholderID, ResponseFormat responseFormat) {
        return show(cardholderID as Integer, responseFormat)
    }

    CardholderRequest show(Integer cardholderID, ResponseFormat responseFormat) {
        cardholders = new Cardholders(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            cardholderID: cardholderID
        )
        return this
    }

    @Override
    CardholderRequest create(Cardholder cardholder) {
        cardholders = new Cardholders(action: Action.ADD_DATA, cardholder: cardholder)
        return this
    }

    @Override
    CardholderRequest update(Cardholder cardholder) {
        cardholders = new Cardholders(
            action: Action.UPDATE_DATA,
            cardholderID: cardholder.cardholderID,
            cardholder: cardholder
        )
        return this
    }

    @Override
    CardholderRequest delete(String cardholderID) {
        delete(cardholderID as Integer)
    }

    CardholderRequest delete(Integer cardholderID) {
        cardholders = new Cardholders(
            action: Action.DELETE_DATA,
            cardholderID: cardholderID
        )
        return this
    }

}
