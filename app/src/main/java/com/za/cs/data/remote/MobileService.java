package com.za.cs.data.remote;

import com.min.core.bean.BaseBean;
import com.min.core.http.SingleHttpClient;
import com.za.cs.BuildConfig;
import com.za.cs.data.model.UpdateBean;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface MobileService {

//    @FormUrlEncoded
//    @POST("/dealer/login.json")
//    Observable<BaseBean<LoginBean>> login(@Field("username") String userName, @Field("userpwd") String userPass);
//
//    @Multipart
//    @POST(BuildConfig.API_UPLOAD_PIC_URL + "/upload/uploadfileforaliyun.do?filesource=10f4fe1edeae11e5b7be086266812821&extname=.jpg")
//    Observable<UploadFileRespBean> uploadFile(@Part MultipartBody.Part namePart, @Part MultipartBody.Part photoPart);
//
//    @Multipart
//    @POST(BuildConfig.API_UPLOAD_PIC_URL + "/upload/uploadfileforaliyun.do?filesource=10f4fe1edeae11e5b7be086266812821&extname=.jpg")
//    Observable<UploadFileRespBean> uploadFile(@Part MultipartBody.Part photoPart);

    @GET(BuildConfig.APP_SELF_UPDATE_URL)
    Observable<UpdateBean> checkUpdate();

    class Creator {

        public static MobileService newMobileService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_SERVER_URL)
                    .client(SingleHttpClient.getHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(MobileService.class);
        }

    }
}
