package com.za.cs.data.remote;

import com.min.core.bean.CSBaseBean;
import com.min.core.http.SingleHttpClient;
import com.min.core.http.interceptor.CSApiInterceptor;
import com.za.cs.BuildConfig;
import com.za.cs.data.model.UpdateBean;
import com.za.cs.data.model.UserInfo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface MobileService {

    @GET(BuildConfig.APP_SELF_UPDATE_URL)
    Observable<UpdateBean> checkUpdate();

    @FormUrlEncoded
    @POST("/user/login")
    Observable<CSBaseBean<UserInfo>> login(@Field("userName") String userName, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/renewal")
    Observable<CSBaseBean<Object>> renewal();

    @POST("/user/logout")
    Observable<CSBaseBean<Object>> logout();

    class Creator {

        public static MobileService newMobileService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_SERVER_URL)
                    .client(SingleHttpClient.getHttpClient(new CSApiInterceptor()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(MobileService.class);
        }

    }
}
