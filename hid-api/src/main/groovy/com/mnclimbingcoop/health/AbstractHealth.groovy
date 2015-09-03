package com.mnclimbingcoop.health

import org.joda.time.LocalDateTime
import org.joda.time.MutablePeriod
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

abstract class AbstractHealth {

    static final String NEVER = 'never'

    protected final PeriodFormatter periodPrinter = new PeriodFormatterBuilder().printZeroNever()
            .appendDays().appendSuffix(" days").appendSeparator(" ")
            .appendHours().appendSuffix(" hours").appendSeparator(" ")
            .appendMinutes().appendSuffix(" minutes").appendSeparator(" ")
            .appendSeconds().appendSuffix(" seconds")
            .toFormatter()

    protected LocalDateTime now() {
        return LocalDateTime.now()
    }

    protected String getDrift(LocalDateTime reference) {
        if (!reference) { return NEVER }
        return periodPrinter.print(
            new MutablePeriod(reference.toDate().time, now().toDate().time)
        )
    }

    abstract boolean isOk()

}
