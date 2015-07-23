package com.mnclimbingcoop.app

import groovy.transform.CompileStatic

import java.util.concurrent.Executor

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@CompileStatic
@Configuration
@EnableAsync
@EnableScheduling
class SchedulerConfiguration implements AsyncConfigurer {

    AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler()
    }

    @Override
    Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(
            corePoolSize: 7,
            maxPoolSize: 42,
            queueCapacity: 11,
            threadNamePrefix: 'mncc-'
        )
        executor.initialize()
        return executor
    }

}
