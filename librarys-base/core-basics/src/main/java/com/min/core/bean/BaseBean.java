package com.min.core.bean;

/**
 * Created by minyangcheng on 2016/5/5.
 */
public class BaseBean<T> {

    public int code;
    public String message;
    public T data;

    public int totalItem;

    public boolean isSuccess() {
        return code == 10000;
    }

    public boolean isSignOut() {
        return false;
    }

}
