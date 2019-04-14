package com.min.core.http;

import com.min.core.helper.HttpsHelper;
import com.min.core.http.interceptor.CGApiInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by minyangcheng on 2018/1/17.
 */

public class SingleHttpClient {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getHttpClient() {
        if (okHttpClient == null) {
            synchronized (SingleHttpClient.class) {
                if (okHttpClient == null) {
                    initHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    private static void initHttpClient() {
        HttpsHelper.SSLParams sslParams = HttpsHelper.getSslSocketFactory(null, null, null);
        HttpsHelper.UnSafeHostnameVerifier hostnameVerifier = new HttpsHelper.UnSafeHostnameVerifier();

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new CGApiInterceptor())
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
    }

}
