package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Credential
import com.mnclimbingcoop.domain.Credentials

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers

@CompileStatic
@Named
@Slf4j
class CredentialSurveyService {

    static final Integer PAGE_SIZE = 10
    protected final CredentialService service
    protected final HidService hidService

    @Inject
    CredentialSurveyService(CredentialService credentialService, HidService hidService) {
        this.service = credentialService
        this.hidService = hidService
    }

    void survey() {
        List<Observable> observables = hidService.doors.collect{ observeCredentials(it).observeOn(Schedulers.io()) }

        Observable.merge(observables).subscribe(
            { Credential credential ->
                // credentials are already added to the state via the CredentialService
                log.info " + adding [#${credential.rawCardNumber}]: " +
                          "${credential.cardNumber} ${credential.formatName}, owner: ${credential.cardholderID}"
            }, { Throwable t ->
                log.error "Error while reading credentials ${t.class} ${t.message}"
            }, {
                log.info "Done retrieving credentials."
                service.sync()
            }
        )
    }

    protected Observable<Credential> observeCredentials(String door) {
        return Observable.create({ Subscriber<Credential> subscriber ->
            Credentials metaData = service.overview(door)
            Integer unassignedCredentials = metaData.unassignedCredentials
            log.info "discovered ${unassignedCredentials} unassigned credentials"
            Integer offset = 0
            while (offset <= unassignedCredentials && !subscriber.unsubscribed) {
                Credentials credentials = service.list(door, offset, PAGE_SIZE)
                if (credentials.credentials) {
                    for (Credential credential : credentials.credentials) {
                        if (subscriber.unsubscribed) { break }
                        subscriber.onNext(credential)
                    }
                }
                Thread.sleep(100)
                offset += PAGE_SIZE
            }

            if (!subscriber.unsubscribed) {
                subscriber.onCompleted()
            }
        } as Observable.OnSubscribe<Credential>)
    }

}
