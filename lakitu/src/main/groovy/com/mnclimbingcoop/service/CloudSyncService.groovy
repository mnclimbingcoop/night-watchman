package com.mnclimbingcoop.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.config.AwsConfiguration
import com.mnclimbingcoop.domain.EdgeSoloState
import com.mnclimbingcoop.domain.VertXRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named
@Slf4j
class CloudSyncService extends AbstractCloudSyncService<VertXRequest, EdgeSoloState> {

    @Inject
    CloudSyncService(AwsConfiguration awsConfig,
                     HealthService healthService,
                     ObjectMapper objectMapper) {
        super(awsConfig, healthService, objectMapper)
        flushCommands = true
        // Writing to this queue is optional, so default health to true
        healthService.health.sqsHealth.sqsWriteOk = true
    }

    @Override
    EdgeSoloState convert(String data) {
        log.trace "converting ${data}"
        return objectMapper.readValue(data, EdgeSoloState)
    }

}
