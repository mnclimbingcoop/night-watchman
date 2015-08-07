package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime

class SqsHealth {

    // SQS Checks
    LocalDateTime lastQueueRead
    LocalDateTime lastQueueWritten
    LocalDateTime lastQueueCheck
    int messagesReceived = 0
    int messagesSent = 0
    boolean sqsReadOk = false
    boolean sqsWriteOk = false

    boolean isOk() {

        LocalDateTime now = LocalDateTime.now()
        LocalDateTime minCheckTime = now.minusMinutes(1)

        return (
            lastQueueCheck > minCheckTime &&
            sqsReadOk && sqsWriteOk
        )
    }

}
