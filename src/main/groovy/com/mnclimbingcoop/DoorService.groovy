package com.mnclimbingcoop

import javax.inject.Inject

class DoorService {

    final RequestBuilder requestBuilder
    final HidEdgeProApi hidEdgeProApi
    final ObjectMapper objectMapper

    @Inject
    DoorService(HidEdgeProApi hidEdgeProApi, XmlMapper objectMapper) {
        this.hidEdgeProApi = hidEdgeProApi
        this.requestBuilder = new RequestBuilder()
        this.objectMapper = objectMapper
    }

    VertXMessage displayRecent() {
        VertXMessage request = requestBuilder.displayRecent()
        String requestXml = objectMapper.writeValueAsString(request)
        VertXMessage response = hidEdgeProApi.get(requestXml)
    }

}
