package com.mnclimbingcoop

import retrofit.http.*

interface HidEdgeProApi {

    @Headers(['Accept: application/xml'])
    @GET('/cgi-bin/vertx_xml.cgi')
    Map poll(@Field("XML") String xml)

}
