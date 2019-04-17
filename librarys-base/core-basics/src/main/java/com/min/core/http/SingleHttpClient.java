package com.min.core.http;

import com.min.core.helper.HttpsHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by minyangcheng on 2018/1/17.
 */

public class SingleHttpClient {

    private static Map<String, OkHttpClient> httpClientMap = new HashMap<>();

    public static OkHttpClient getHttpClient(Interceptor interceptor) {
        OkHttpClient httpClient = httpClientMap.get(interceptor.getClass().getName());
        if (httpClient == null) {
            httpClient = createHttpClient(interceptor);
            httpClientMap.put(interceptor.getClass().getName(), httpClient);
        }
        return httpClient;
    }

    private static OkHttpClient createHttpClient(Interceptor interceptor) {
        HttpsHelper.SSLParams sslParams = HttpsHelper.getSslSocketFactory(null, null, null);
        HttpsHelper.UnSafeHostnameVerifier hostnameVerifier = new HttpsHelper.UnSafeHostnameVerifier();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

}
