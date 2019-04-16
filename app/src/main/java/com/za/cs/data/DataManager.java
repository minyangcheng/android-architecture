package com.za.cs.data;

import com.za.cs.data.local.PreferencesHelper;
import com.za.cs.data.remote.MobileService;

/**
 * Created by minyangcheng on 2017/9/19.
 */

public class DataManager {

    private static PreferencesHelper preferencesHelper;
    private static MobileService mobileService;

    public static MobileService getMobileService() {
        if (mobileService == null) {
            mobileService = MobileService.Creator.newMobileService();
        }
        return mobileService;
    }

    public static PreferencesHelper getPreferencesHelper() {
        if (preferencesHelper == null) {
            preferencesHelper = new PreferencesHelper();
        }
        return preferencesHelper;
    }


}
