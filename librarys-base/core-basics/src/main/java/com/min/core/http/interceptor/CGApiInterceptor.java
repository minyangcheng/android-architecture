package com.min.core.http.interceptor;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.min.core.CoreConstants;
import com.min.core.helper.GsonHelper;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class CGApiInterceptor implements Interceptor {

    public static final String FORM = "application/x-www-form-urlencoded";

    public static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = addMobileInfoToReqHeader(request);

        RequestBody requestBody = request.body();
        if (request.method().equalsIgnoreCase("GET") || !isFormUrlEncoded(requestBody)) {
            //请求参数打印
            LogUtils.dTag(CoreConstants.HTTP_LOG, request.url().toString());
            return chain.proceed(request);
        }

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        String paramStr = buffer.readUtf8();
        paramStr = URLDecoder.decode(paramStr, "utf-8");

        Map<String, String> map = splitStrToMap(paramStr);
        handleParams(map);
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        request = request.newBuilder()
                .post(formBuilder.build())
                .build();
        //请求参数打印
        LogUtils.dTag(CoreConstants.HTTP_LOG, GsonHelper.toPrettyJson(map));
        Response response = chain.proceed(request);
        return response;
    }

    private Request addMobileInfoToReqHeader(Request request) {
        String jsonString = "";
        try {
            Head head = new Head();
            head.apiVersion = CoreConstants.VERSION_NAME;
            head.mobileVersion = CoreConstants.SDK_INT;
            head.channel = CoreConstants.FLAVOR;
            head.time = mDateFormat.format(new Date());

            jsonString = GsonHelper.toJson(head);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        request = request.newBuilder()
                .addHeader("MobileInfo", jsonString)
                .build();
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

    private String mapToString(Map<String, String> map) {
        String formData = "";
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            encodedParams.append(entry.getKey());
            encodedParams.append('=');
            encodedParams.append(entry.getValue());
            encodedParams.append('&');
        }
        formData = encodedParams.toString();
        if (formData.endsWith("&")) {
            formData = formData.substring(0, formData.lastIndexOf("&"));
        }
        return formData;
    }

    public void handleParams(Map<String, String> params) {
        params.put("source", CoreConstants.SOURCE);
        String mapUrl = mapToString(params);
        params.put("signature", EncryptUtils.encryptMD5ToString(mapUrl + EncryptUtils.encryptMD5ToString(CoreConstants.APP_SECRET)));
    }

    class Head {
        public String apiVersion;
        public String mobileVersion;
        public String channel;
        public String time;
        public String token;
    }
}
