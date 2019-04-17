package com.min.sample.data.remote;

import com.min.core.bean.BaseBean;
import com.min.core.helper.HttpsHelper;
import com.min.core.http.SingleHttpClient;
import com.min.core.http.interceptor.CGApiInterceptor;
import com.min.sample.BuildConfig;
import com.min.sample.data.model.LoginBean;
import com.min.sample.data.model.UploadFileRespBean;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface MobileService {

    @FormUrlEncoded
    @POST("/dealer/login.json")
    Observable<BaseBean<LoginBean>> login(@Field("username") String userName, @Field("userpwd") String userPass);

    @Multipart
    @POST(BuildConfig.API_UPLOAD_PIC_URL + "/upload/uploadfileforaliyun.do?filesource=10f4fe1edeae11e5b7be086266812821&extname=.jpg")
    Observable<UploadFileRespBean> uploadFile(@Part MultipartBody.Part namePart, @Part MultipartBody.Part photoPart);

    @Multipart
    @POST(BuildConfig.API_UPLOAD_PIC_URL + "/upload/uploadfileforaliyun.do?filesource=10f4fe1edeae11e5b7be086266812821&extname=.jpg")
    Observable<UploadFileRespBean> uploadFile(@Part MultipartBody.Part photoPart);

    class Creator {

        public static MobileService newMobileService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_SERVER_URL)
                    .client(SingleHttpClient.getHttpClient(new CGApiInterceptor()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(MobileService.class);
        }

    }
}
