package com.mnclimbingcoop.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Credentials
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.type.Action
import com.mnclimbingcoop.domain.type.ResponseFormat

import org.joda.time.LocalDateTime

@JacksonXmlRootElement(localName='VertXMessage')
class CredentialRequest extends VertXRequest implements EntityCollectionRequest<CredentialRequest, Credential> {

    CredentialRequest() {
        credentials = new Credentials(action: Action.DESCRIBE_RECORDS)
    }

    @Override
    CredentialRequest overview() {
        credentials = new Credentials(action: Action.DESCRIBE_RECORDS)
        return this
    }

    @Override
    CredentialRequest list(Integer offset, Integer count) {
        return list(offset, count, ResponseFormat.EXPANDED)
    }

    CredentialRequest list(Integer offset,
                            Integer count,
                            ResponseFormat responseFormat) {
        credentials = new Credentials(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            recordOffset: offset,
            recordCount: count
        )
        if (!responseFormat) { credentials.cardholderID = '' }
        return this
    }

    CredentialRequest show(String rawCardNumber) {
        return show(rawCardNumber, ResponseFormat.EXPANDED)
    }

    @Override
    CredentialRequest show(String rawCardNumber, ResponseFormat responseFormat) {
        credentials = new Credentials(
            action: Action.LIST_RECORDS,
            responseFormat: responseFormat,
            isCard: true,
            rawCardNumber: rawCardNumber
        )
        return this
    }

    @Override
    CredentialRequest create(Credential credential) {
        credentials = new Credentials(action: Action.ADD_DATA, credential: credential)
        return this
    }

    @Override
    CredentialRequest update(Credential credential) {
        credentials = new Credentials(
            action: Action.UPDATE_DATA,
            rawCardNumber: credential.rawCardNumber,
            isCard: true,
            credential: credential
        )
        return this
    }

    @Override
    CredentialRequest delete(String rawCardNumber) {
        credentials = new Credentials(
            action: Action.DELETE_DATA,
            rawCardNumber: rawCardNumber,
            isCard: true
        )
        return this
    }

    CredentialRequest bulkCreate() {
        // TODO
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

    CredentialRequest updateCardholder(String rawCardNumber, Integer cardholderID) {
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

}
