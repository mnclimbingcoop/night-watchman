package com.mnclimbingcoop.service

import com.fasterxml.jackson.databind.ObjectMapper

import com.mnclimbingcoop.config.AwsConfiguration
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.health.Health

import groovy.util.logging.Slf4j

import org.springframework.scheduling.annotation.Async

import javax.inject.Inject
import javax.inject.Named

import rx.schedulers.Schedulers

@Named
@Slf4j
class HeartBeatService extends AbstractCloudSyncService<String, Health> {

    @Inject
    HeartBeatService(AwsConfiguration awsConfig,
                     HealthService healthService,
                     ObjectMapper objectMapper) {
        super(awsConfig.region, null, awsConfig.sqs.healthQueue, healthService, objectMapper)
    }

    @Override
    Health convert(String data) {
        log.trace "converting ${data}"
        return objectMapper.readValue(data, Health)
    }

    @Async
    void takePulse() {
        log.info "taking pulse of night watchman health."
        observable.cast(Health).subscribeOn(Schedulers.io()).subscribe(
            { Health health ->
                healthService.updateDependentHealth(health)
                log.debug "Received heart beat."
            }, { Throwable t ->
                log.error "Error while heartbeat from night watchman ${t.message}", t
                healthService.checkMessagesFailed()
            }, {
                log.error 'Health check stream stopped!'
            }
        )
    }


}

