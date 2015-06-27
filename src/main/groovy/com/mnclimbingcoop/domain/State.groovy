package com.mnclimbingcoop.domain

import groovy.transform.CompileStatic

@CompileStatic
enum State {

    set(true),
    unset(false)

    final boolean value

    State(boolean value) {
        this.value = value
    }

}
