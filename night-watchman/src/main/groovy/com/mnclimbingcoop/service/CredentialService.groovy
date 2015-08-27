package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Credentials
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.domain.VertXResponse
import com.mnclimbingcoop.request.CredentialRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CredentialService {

    protected final HidService hidService

    @Inject
    CredentialService(HidService hidService) {
        this.hidService = hidService
    }

    Map<String, Credentials> overview() {
        VertXRequest request = new CredentialRequest().overview()
        return hidService.getAll(request) { String name, VertXResponse resp ->
            return [ name, resp.credentials ]
        }
    }

    Credentials overview(String name) {
        VertXRequest request = new CredentialRequest().overview()
        return hidService.get(name, request)?.credentials

    }

    Credentials list(String name, Integer offset, Integer count) {
        VertXRequest request = new CredentialRequest().list(offset, count, null)
        Credentials credentials = hidService.get(name, request)?.credentials
        if (credentials?.credentials) {
            hidService.hidStates[name].credentials.addAll(credentials.credentials)
        }
        return credentials
    }

    Credential show(String name, String id) {
        VertXRequest request = new CredentialRequest().show(id)
        Credential credential =  hidService.get(name, request)?.credentials?.credential
        if (credential) {
            hidService.hidStates[name].credentials << credential
        }
        return credential
    }

    void sync() {
        hidService.sync()
    }

}
