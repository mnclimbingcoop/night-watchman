package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime

class SqsHealth extends AbstractHealth {

    // SQS Checks
    LocalDateTime lastQueueRead
    LocalDateTime lastQueueWritten
    LocalDateTime lastQueueCheck
    int messagesReceived = 0
    int messagesSent = 0
    boolean sqsReadOk = false
    boolean sqsWriteOk = false

    String getQueueReadDrift() {
        getDrift(lastQueueRead)
    }

    String getQueueCheckDrift() {
        getDrift(lastQueueCheck)
    }

    String getQueueWriteDrift() {
        getDrift(lastQueueWritten)
    }

    @Override
    boolean isOk() {

        LocalDateTime minCheckTime = now().minusMinutes(1)

        return (
            lastQueueCheck > minCheckTime &&
            sqsReadOk && sqsWriteOk
        )
    }

}
