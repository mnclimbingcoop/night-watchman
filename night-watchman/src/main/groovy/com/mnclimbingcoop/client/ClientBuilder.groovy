package com.mnclimbingcoop.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.mnclimbingcoop.ObjectMapperBuilder

import groovy.transform.CompileStatic

import javax.net.ssl.SSLContext

import retrofit.RequestInterceptor
import retrofit.RestAdapter
import retrofit.client.OkClient
import retrofit.converter.Converter
import retrofit.converter.JacksonConverter

import com.squareup.okhttp.OkHttpClient

@CompileStatic
class ClientBuilder {

    private String username
    private String password
    OkHttpClient httpClient = new OkHttpClient()

    private boolean unsafeSSL = false;

    String uri = 'http://localhost:8080'
    ObjectMapper objectMapper = ObjectMapperBuilder.buildXml()

    /** One of: NONE, BASIC, HEADERS, HEADERS_AND_ARGS, or FULL */
    RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.BASIC

    ClientBuilder withEndpoint(String uri) {
        this.uri = uri
        return this
    }

    ClientBuilder withObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper
        return this
    }

    ClientBuilder withAuthentication(final String username, final String password) {
        this.username = username
        this.password = password
        return this
    }

    ClientBuilder withUnsafeSSL2() {
        UnsafeClientBuilder unsafeBuilder = new UnsafeClientBuilder()
        httpClient.setSslSocketFactory(unsafeBuilder.unsafeSSLContext)
        httpClient.setHostnameVerifier(unsafeBuilder.unsafeHostnameVerifier)
        return this
    }

    ClientBuilder withUnsafeSSL() {
        SSLContext sslContext
        sslContext = SSLContext.getInstance('TLS')
        sslContext.init(null, null, null)
        httpClient.setSslSocketFactory(sslContext.getSocketFactory())

        return this
    }


    @SuppressWarnings('UnnecessaryPublicModifier')
    public <T> T build(Class<T> clazz) {
        Converter converter = new JacksonConverter(objectMapper)
        RestAdapter.Builder builder = new RestAdapter.Builder()
                                                     .setConverter(converter)
                                                     .setEndpoint(uri)
                                                     .setLog(new RetrofitLogger())
                                                     .setLogLevel(logLevel)
                                                     .setClient(new OkClient(httpClient))

        if (username && password) {
            builder.setRequestInterceptor(new BasicAuthInterceptor(username, password))
        }

        RestAdapter restAdapter = builder.build()

        return (T) restAdapter.create(clazz)
    }

    private class BasicAuthInterceptor implements RequestInterceptor {

        private final String authString

        BasicAuthInterceptor(final String username, final String password) {
            final String credentials = "${username}:${password}"
            authString = "Basic ${Base64.encoder.encodeToString(credentials.bytes)}"
        }

        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Authorization", authString)
        }
    }
}
