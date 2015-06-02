package com.mnclimbingcoop.client

import com.mnclimbingcoop.domain.VertXMessage

import retrofit.http.*

interface HidEdgeProApi {

    @Headers(['Accept: application/xml'])
    @GET('/cgi-bin/vertx_xml.cgi')
    VertXMessage poll(@Field("XML") String xml)

}
