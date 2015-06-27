package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Credentials
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

class CredentialRequest extends VertXRequest {

    CredentialRequest() {
        credentials = new Credentials(action: Action.DESCRIBE_RECORDS)
        return this
    }

    CredentialRequest overview() {
        credentials = new Credentials(action: Action.DESCRIBE_RECORDS)
        return this
    }

    CredentialRequest list(Integer offset = 0,
                            Integer count = 10,
                            ResponseFormat responseFormat = ResponseFormat.EXPANDED) {
        credentials = new Credentials(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            recordOffset: offset,
            recordCount: count
        )
        return this
    }


    CredentialRequest show(Integer rawCardNumber,
                           ResponseFormat responseFormat = ResponseFormat.EXPANDED) {
        credentials = new Credentials(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            isCard: true,
            rawCardNumber: rawCardNumber
        )
        return this
    }

    CredentialRequest create(Credential credential) {
        credentials = new Credentials(action: Action.ADD_DATA, credential: credential)
        return this
    }

    CredentialRequest updateExpiration(String rawCardNumber, LocalDateTime endTime) {
        credentials = new Credentials(
            action: Action.UPDATE_DATA,
            rawCardNumber: rawCardNumber,
            isCard: true,
            credential: new Credential(endTime: endTime)
        )
        return this
    }

    CredentialRequest updateCardHolder(String rawCardNumber, Integer cardholderID) {
        credentials = new Credentials(
            action: Action.UPDATE_DATA,
            rawCardNumber: rawCardNumber,
            isCard: true,
            credential: new Credential(cardholderID: cardholderID.toString())
        )
        return this
    }

    CredentialRequest removeCardHolder(String rawCardNumber) {
        credentials = new Credentials(
            action: Action.UPDATE_DATA,
            rawCardNumber: rawCardNumber,
            isCard: true,
            credential: new Credential(
                cardholderID: ''
            )
        )
        return this
    }


    CredentialRequest delete(String rawCardNumber) {
        credentials = new Credentials(
            action: Action.DELETE_DATA,
            rawCardNumber: rawCardNumber,
            isCard: true
        )
        return this
    }

}
