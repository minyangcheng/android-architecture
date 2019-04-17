package com.min.core.http.interceptor;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.min.common.util.AppUtils;
import com.min.common.util.BuildConfigUtils;
import com.min.common.util.GsonUtils;
import com.min.common.util.LogUtils;
import com.min.common.util.SPStaticUtils;
import com.min.core.CoreConstants;
import com.min.core.http.SingleHttpClient;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class CSApiInterceptor implements Interceptor {

    private String FORM = "application/x-www-form-urlencoded";

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CSApiInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if(isAuthIdExpired()){
            LogUtils.dTag(CoreConstants.HTTP_LOG, "[ HTTP POST ]---> request new auth id");
            Request newAuthRequest = new Request.Builder()
                    .url(BuildConfigUtils.getConfigStr("API_SERVER_URL")+"/user/renewal")
                    .post(RequestBody.create(null,""))
                    .addHeader("Auth-Id",SPStaticUtils.getString("authId", ""))
                    .build();
            Call call = SingleHttpClient.getHttpClient(this).newCall(newAuthRequest);
            Response response = call.execute();
            if(response.isSuccessful()){
                String jsonStr = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
                jsonObject = jsonObject.getAsJsonObject("model");
                SPStaticUtils.put("userInfo", jsonObject.toString());
                jsonObject = jsonObject.getAsJsonObject("token");
                SPStaticUtils.put("authId", jsonObject.get("authId").toString());
                SPStaticUtils.put("expireDate", jsonObject.get("expireDate").toString());
            }
        }
        Request request = chain.request();

        request = handleHeader(request);

        if (request.method().equalsIgnoreCase("POST") && isFormUrlEncoded(request.body())) {
            request = handleRequestBody(request);
        } else if (request.method().equalsIgnoreCase("GET")) {
            LogUtils.dTag(CoreConstants.HTTP_LOG, "[ HTTP GET ]---> " + request.url());
        } else if (request.method().equalsIgnoreCase("POST")) {
            LogUtils.dTag(CoreConstants.HTTP_LOG, "[ HTTP POST ]---> " + request.url());
        }

        Response response = chain.proceed(request);
        return response;
    }

    private boolean isAuthIdExpired() {
        long expireDate = SPStaticUtils.getLong("expireDate");
        if (expireDate > -1) {
            return (System.currentTimeMillis() - expireDate) >= 0;
        }
        return false;
    }

    private Request handleHeader(Request request) {
        MobileInfo mobileInfo = new MobileInfo();
        mobileInfo.os = "android";
        mobileInfo.version = AppUtils.getAppVersionName();
        mobileInfo.channel = BuildConfigUtils.getConfigStr("FLAVOR");
        mobileInfo.time = mDateFormat.format(new Date());
        String authId = SPStaticUtils.getString("authId", "");
        request = request.newBuilder()
                .addHeader("MobileInfo", GsonUtils.toJson(mobileInfo))
                .addHeader("Auth-Id", authId)
                .build();
        return request;
    }

    private Request handleRequestBody(Request request) throws IOException {
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        String str = URLDecoder.decode(buffer.readUtf8(), "utf-8");
        Map<String, String> paramsMap = splitStrToMap(str);
        request = request.newBuilder()
                .post(RequestBody.create(MediaType.parse("application/json"), GsonUtils.toJson(paramsMap)))
                .build();
        LogUtils.dTag(CoreConstants.HTTP_LOG, "[ HTTP POST ] ---> " + request.url(), GsonUtils.toPrettyJson(paramsMap));
        return request;
    }

    private boolean isFormUrlEncoded(RequestBody requestBody) {
        if (requestBody != null) {
            if (requestBody.contentType() != null) {
                String mediaTypeStr = requestBody.contentType().toString();
                if (!TextUtils.isEmpty(mediaTypeStr) && mediaTypeStr.equals(FORM)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<String, String> splitStrToMap(String originalStr) {
        Map<String, String> map = new TreeMap<>();
        if (TextUtils.isEmpty(originalStr)) return map;
        String[] keyValueArr = originalStr.split("&");
        for (String keyValue : keyValueArr) {
            String[] entry = keyValue.split("=");
            if (entry.length <= 1) continue;
            map.put(entry[0], entry[1]);
        }
        return map;
    }

    public static class MobileInfo {
        public String os;
        public String version;
        public String channel;
        public String time;
    }

}
