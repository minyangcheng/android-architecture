package com.min.common.widget.upload;

/**
 * Created by minyangcheng on 2019/4/12.
 */
public class UploadImageBean<T> {

    public static final int UPLOAD_PREPARED = 0;
    public static final int UPLOAD_ING = 1;
    public static final int UPLOAD_SUCCESS = 2;
    public static final int UPLOAD_FAIL = 3;
    public static final int UPLOAD_DISABLED = 4;

    public int status;
    public int resId;
    public String path;
    public String url;
    public String name;
    public boolean isAdd;

    public String fileSize;

    public String filePath;
    public String fileUrl;

    public String latitude;
    public String longitude;

    public T relationObj;

    public UploadImageBean() {
    }

    public UploadImageBean(int resId, String name) {
        this.resId = resId;
        this.name = name;
        this.status = UPLOAD_PREPARED;
    }

    public UploadImageBean(String path, String name) {
        this.path = path;
        this.name = name;
        this.status = UPLOAD_ING;
    }

    public static UploadImageBean newInstanceForImagePrepared(String name, int resId) {
        UploadImageBean uploadImageBean = new UploadImageBean();
        uploadImageBean.name = name;
        uploadImageBean.resId = resId;
        uploadImageBean.status = UPLOAD_PREPARED;
        return uploadImageBean;
    }

    public static UploadImageBean newInstanceForImageSuccess(String name, int resId, String url) {
        UploadImageBean uploadImageBean = new UploadImageBean();
        uploadImageBean.name = name;
        uploadImageBean.resId = resId;
        uploadImageBean.url = url;
        uploadImageBean.status = UPLOAD_SUCCESS;
        return uploadImageBean;
    }

    public static UploadImageBean newInstanceForAddImageSuccess(String name, int resId, String url) {
        UploadImageBean uploadImageBean = newInstanceForImageSuccess(name, resId, url);
        uploadImageBean.isAdd = true;
        return uploadImageBean;
    }

}
