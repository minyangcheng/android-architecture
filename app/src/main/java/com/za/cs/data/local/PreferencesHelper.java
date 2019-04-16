package com.za.cs.data.local;

import android.text.TextUtils;

import com.min.common.util.AppUtils;
import com.min.common.util.GsonUtils;
import com.min.common.util.SPUtils;
import com.za.cs.app.AppConstants;
import com.za.cs.data.model.UserInfo;

public class PreferencesHelper {

    private SPUtils spUtils;

    public PreferencesHelper() {
        spUtils = SPUtils.getInstance(AppUtils.getAppPackageName());
    }

    public void setUserInfo(UserInfo userInfo) {
        spUtils.put(AppConstants.KEY_USER_INFO, GsonUtils.toJson(userInfo));
    }

    public UserInfo getUserInfo() {
        String json = spUtils.getString(AppConstants.KEY_USER_INFO);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return GsonUtils.fromJson(json, UserInfo.class);
        }
    }

    public void setHasLogin(boolean flag) {
        spUtils.put(AppConstants.KEY_HAS_LOGIN, flag);
    }

    public boolean getHasLogin() {
        return spUtils.getBoolean(AppConstants.KEY_HAS_LOGIN, false);
    }

}
