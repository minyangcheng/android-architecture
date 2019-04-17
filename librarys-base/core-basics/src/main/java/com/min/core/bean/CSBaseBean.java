package com.min.core.bean;

/**
 * Created by minyangcheng on 2016/5/5.
 */
public class CSBaseBean<T> {

    public boolean success;
    public String resultCode;
    public String message;
    public T model;

    public boolean isSuccess() {
        return success;
    }

    public boolean isSignOut() {
        return false;
    }

}
