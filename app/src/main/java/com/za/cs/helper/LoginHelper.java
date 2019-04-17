package com.za.cs.helper;

import com.za.cs.data.DataManager;
import com.za.cs.data.model.UserInfo;

/**
 * Created by minych on 2019/4/17 14:01
 */
public class LoginHelper {

    public static void login(String userName, String password, UserInfo userInfo) {
        DataManager.getPreferencesHelper().setHasLogin(true);
        DataManager.getPreferencesHelper().setUserName(userName);
        DataManager.getPreferencesHelper().setPassword(password);
        DataManager.getPreferencesHelper().setUserInfo(userInfo);
        DataManager.getPreferencesHelper().setAuthId(userInfo.token.authId);
        DataManager.getPreferencesHelper().setExpireDate(userInfo.token.expireDate);
    }

    public static void logout() {
        DataManager.getPreferencesHelper().setHasLogin(false);
        DataManager.getPreferencesHelper().setUserInfo(null);
        DataManager.getPreferencesHelper().setAuthId("");
        DataManager.getPreferencesHelper().setExpireDate(-1);
    }

}
