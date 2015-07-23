package com.mnclimbingcoop.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @RequestMapping(value = '/ping', method = RequestMethod.GET, produces = 'application/json')
    Map ping() {
        [message: 'Pong']
    }

}

