package com.min.core.helper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartHelper {

    public static final String IMAGE = "image/*";
    public static final String TEXT = "text/*";
    public static final String VIDEO = "video/*";
    public static final String AUDIO = "audio/*";

    public static MultipartBody.Part keyValuePart(String key, String value) {
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, value);
        return part;
    }

    public static MultipartBody.Part imageMultiPart(String key, File imageFile) {
        return fileMultiPart(key, imageFile, IMAGE);
    }

    public static MultipartBody.Part fileMultiPart(String key, File value, String type) {
        MediaType mediaType = MediaType.parse(type);
        RequestBody requestBody = RequestBody.create(mediaType, value);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, value.getName(), requestBody);
        return part;
    }


}
