package com.mnclimbingcoop

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @RequestMapping('/ping')
    Map ping() {
        [message: 'Pong']
    }

}

