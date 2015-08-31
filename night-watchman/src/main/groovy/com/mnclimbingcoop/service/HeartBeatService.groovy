package com.mnclimbingcoop.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.config.AwsConfiguration
import com.mnclimbingcoop.domain.VertXRequest
import com.mnclimbingcoop.health.Health

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Scheduled

@CompileStatic
@Named
@Slf4j
class HeartBeatService extends AbstractCloudSyncService<Health, String> {

    @Inject
    HeartBeatService(AwsConfiguration awsConfig,
                     HealthService healthService,
                     ObjectMapper objectMapper) {
        super(awsConfig.region, awsConfig.sqs.healthQueue, null, healthService, objectMapper)
        flushCommands = true
    }

    @Scheduled(fixedDelayString = '${schedule.health.rate}', initialDelayString = '${schedule.health.initialDelay}')
    void breath() {
        log.info "Heartbeat."
        sendSqsMessage(healthService.health)
    }

    @Override
    String convert(String data) {
        return data
    }

}
