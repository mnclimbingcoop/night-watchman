package com.mnclimbingcoop.client

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

import java.security.SecureRandom

class UnsafeClientBuilder {

    SSLSocketFactory getUnsafeSSLContext() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = [
            new X509TrustManager() {
                @Override
                void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null
                }
            }
        ]

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance('SSL')
        sslContext.init(null, trustAllCerts, new SecureRandom())

        return sslContext.socketFactory
    }

    HostnameVerifier getUnsafeHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }
    }

}
