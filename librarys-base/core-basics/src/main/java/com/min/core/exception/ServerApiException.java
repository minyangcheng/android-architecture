package com.min.core.exception;

import com.min.core.bean.BaseBean;

/**
 * Created by minyangcheng on 2016/10/8.
 */
public class ServerApiException extends RuntimeException{

    private String code;
    private String message;

    public ServerApiException(String code,String message) {
        super(message);
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
