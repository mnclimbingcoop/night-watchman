package com.mnclimbingcoop.service

import com.mnclimbingcoop.domain.Error

class HidRemoteErrorException extends Exception {

    final Error error

    HidRemoteErrorException(String message, Error error) {
        super(message)
        this.error = error
    }
}
