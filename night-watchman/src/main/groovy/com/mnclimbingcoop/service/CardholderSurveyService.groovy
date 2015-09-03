package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Cardholder
import com.mnclimbingcoop.domain.Cardholders

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
class CardholderSurveyService {

    static final Integer PAGE_SIZE = 10
    protected final CardholderService service
    protected final HidService hidService

    @Inject
    CardholderSurveyService(CardholderService cardholderService, HidService hidService) {
        this.service = cardholderService
        this.hidService = hidService
    }

    void survey() {
        List<Observable> observables = hidService.doors.collect{ observeCardholders(it).observeOn(Schedulers.io()) }

        Observable.merge(observables).subscribe(
            { Cardholder cardholder ->
                // cardholders are already added to the state via the CardholderService
                log.info " + adding [#${cardholder.cardholderID}]: ${cardholder.forename} ${cardholder.surname}"
            }, { Throwable t ->
                log.error "Error while reading card holders ${t.class} ${t.message}"
            }, {
                log.info "Done retrieving card holders."
                service.sync()
            }
        )
    }

    protected Observable<Cardholder> observeCardholders(String door) {
        return Observable.create({ Subscriber<Cardholder> subscriber ->
            Cardholders metaData = service.overview(door)
            Integer inUse = metaData.inUse
            log.info "discovered ${inUse} cardholders"
            Integer offset = 0
            while (offset <= inUse && !subscriber.unsubscribed) {
                Cardholders cardholders = service.list(door, offset, PAGE_SIZE)
                if (cardholders.cardholders) {
                    for (Cardholder cardholder : cardholders.cardholders) {
                        if (subscriber.unsubscribed) { break }
                        subscriber.onNext(cardholder)
                    }
                }
                Thread.sleep(100)
                offset += PAGE_SIZE
            }

            if (!subscriber.unsubscribed) {
                subscriber.onCompleted()
            }
        } as Observable.OnSubscribe<Cardholder>)
    }

}
