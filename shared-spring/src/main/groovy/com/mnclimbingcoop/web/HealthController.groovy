package com.mnclimbingcoop.web

import com.mnclimbingcoop.health.Health
import com.mnclimbingcoop.health.HealthCheckFailureException
import com.mnclimbingcoop.service.HealthService

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping('/health')
@Slf4j
class HealthController {

    protected final HealthService healthService

    @Inject
    HealthController(HealthService healthService) {
        this.healthService = healthService
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/plain')
    String getHealth() {
        if (!healthService.currentHealth.ok) {
            throw new HealthCheckFailureException()
        }
        return 'OK'
    }

    @RequestMapping(value = '/status', method = RequestMethod.GET, produces = 'application/json')
    Health getStatus() {
        return healthService.currentHealth
    }

    @ExceptionHandler(HealthCheckFailureException)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    String handle(HealthCheckFailureException e) {
        return 'CRITICAL'
    }

}
