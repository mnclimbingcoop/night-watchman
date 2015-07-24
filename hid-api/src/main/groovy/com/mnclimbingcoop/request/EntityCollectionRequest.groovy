package com.mnclimbingcoop.request

import com.mnclimbingcoop.domain.type.ResponseFormat

interface EntityCollectionRequest<S,T> {

    abstract S overview()

    abstract S list(Integer offset, Integer count)

    abstract S show(String id, ResponseFormat responseFormat)

    abstract S create(T item)

    abstract S update(T item)

    abstract S delete(String id)

}
