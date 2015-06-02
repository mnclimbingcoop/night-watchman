package com.mnclimbingcoop.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum State {

    set(true),
    unset(false)

    final boolean value

    State(boolean value) {
        this.value = value
    }

}
