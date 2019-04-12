package com.min.sample.data.remote;

import com.min.core.base.BaseApp;
import com.min.core.bean.BaseBean;
import com.min.core.helper.HttpsHelper;
import com.min.core.http.interceptor.CGApiInterceptor;
import com.min.sample.BuildConfig;
import com.min.sample.data.model.InfoBean;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MobileService {

    String BASE_URL = BuildConfig.API_SERVER_URL;

    @FormUrlEncoded
    @POST("/mobile/getTransInfo.json")
    Observable<BaseBean<List<InfoBean>>> getTransInfoList(@Field("merchantNo") String merchantNo);

    class Creator {

        public static MobileService newMobileService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(MobileService.class);
        }

        public static OkHttpClient getOkHttpClient() {

            Cache cache = new Cache(new File(BaseApp.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 30);

            HttpsHelper.SSLParams sslParams = HttpsHelper.getSslSocketFactory(null, null, null);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(new CGApiInterceptor())
                    .retryOnConnectionFailure(true)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .sslSocketFactory(sslParams.sSLSocketFactory)
                    .build();
            return okHttpClient;
        }

    }
}
