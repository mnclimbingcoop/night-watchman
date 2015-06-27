package com.mnclimbingcoop.client

import com.mnclimbingcoop.domain.VertXResponse

import retrofit.http.*

interface HidEdgeProApi {

    @Headers(['Accept: application/xml'])
    @GET('/cgi-bin/vertx_xml.cgi')
    VertXResponse get(@Query("XML") String xml)

}
