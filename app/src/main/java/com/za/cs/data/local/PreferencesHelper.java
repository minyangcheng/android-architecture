package com.za.cs.data.local;

import android.text.TextUtils;

import com.min.common.util.GsonUtils;
import com.min.common.util.SPStaticUtils;
import com.za.cs.data.model.UserInfo;

public class PreferencesHelper {

    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_HAS_LOGIN = "hasLogin";
    private static final String KEY_USER_INFO = "userInfo";
    private static final String KEY_AUTH_ID = "authId";
    private static final String KEY_EXPIRE_DATE = "expireDate";

    public PreferencesHelper() {
    }

    public void setUserName(String userName) {
        SPStaticUtils.put(KEY_USER_NAME, userName);
    }

    public String getUserName() {
        return SPStaticUtils.getString(KEY_USER_NAME);
    }

    public void setPassword(String password) {
        SPStaticUtils.put(KEY_PASSWORD, password);
    }

    public String getPassword() {
        return SPStaticUtils.getString(KEY_PASSWORD);
    }

    public void setUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            SPStaticUtils.put(KEY_USER_INFO, "");
        } else {
            SPStaticUtils.put(KEY_USER_INFO, GsonUtils.toJson(userInfo));
        }
    }

    public UserInfo getUserInfo() {
        String json = SPStaticUtils.getString(KEY_USER_INFO);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return GsonUtils.fromJson(json, UserInfo.class);
        }
    }

    public void setHasLogin(boolean flag) {
        SPStaticUtils.put(KEY_HAS_LOGIN, flag);
    }

    public boolean getHasLogin() {
        return SPStaticUtils.getBoolean(KEY_HAS_LOGIN, false);
    }

    public void setAuthId(String authId) {
        SPStaticUtils.put(KEY_AUTH_ID, authId);
    }

    public void setExpireDate(long expireDate) {
        SPStaticUtils.put(KEY_EXPIRE_DATE, expireDate);
    }

    public long getExpireDate() {
        return SPStaticUtils.getLong(KEY_EXPIRE_DATE, -1);
    }

}
