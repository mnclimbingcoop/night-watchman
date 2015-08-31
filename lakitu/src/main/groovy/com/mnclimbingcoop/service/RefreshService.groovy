package com.mnclimbingcoop.service

import com.amazonaws.services.sqs.model.SendMessageResult
import com.mnclimbingcoop.domain.AccessHolder
import com.mnclimbingcoop.domain.Meta
import com.mnclimbingcoop.domain.VertXRequest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

import org.springframework.scheduling.annotation.Async

@CompileStatic
@Named
@Slf4j
class RefreshService {

    protected final CloudSyncService cloudSyncService

    @Inject
    RefreshService(CloudSyncService cloudSyncService) {
        this.cloudSyncService = cloudSyncService
    }

    @PostConstruct
    void setup() {
        requestRefreshAsync()
    }

    @Async
    void requestRefreshAsync() {
        log.info "Waiting 10 seconds before requesting data refresh"
        Thread.sleep(10000)
        log.info "Requesting data refresh"
        SendMessageResult result = requestRefresh()
        log.info "Refresh request sent: ${result}"
    }

    SendMessageResult requestRefresh() {
        VertXRequest request = new VertXRequest()
        request.meta = new Meta(refresh: true)
        return cloudSyncService.sendSqsMessage(request)
    }

}
