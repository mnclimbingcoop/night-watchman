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
        quiet = true
    }

    @Override
    Health convert(String data) {
        return objectMapper.readValue(data, Health)
    }

    @Async
    void takePulse() {
        log.info "taking pulse of night watchman health."
        observable.cast(Health).subscribeOn(Schedulers.io()).subscribe(
            { Health health ->
                healthService.heartbeat()
                healthService.updateDependentHealth(health)
            }, { Throwable t ->
                log.error "Error while taking heartbeat from night watchman ${t.message}", t
                healthService.checkMessagesFailed()
            }, {
                log.error 'Health check stream stopped!'
            }
        )
    }


}

