package com.min.sample.data.local;

import com.blankj.utilcode.util.SPUtils;
import com.min.sample.app.AppConstants;

public class PreferencesHelper {

    private SPUtils spUtils;

    public PreferencesHelper() {
        spUtils = SPUtils.getInstance();
    }

    public String getLocationStr() {
        return spUtils.getString(AppConstants.KEY_LOCATION);
    }

    public void putLocationStr(String locationStr) {
        spUtils.put(AppConstants.KEY_LOCATION, locationStr);
    }

    public String getTerminalInfoStr() {
        return spUtils.getString(AppConstants.KEY_TERMINAL_INFO);
    }

    public void putTerminalInfoStr(String terminalInfoStr) {
        spUtils.put(AppConstants.KEY_TERMINAL_INFO, terminalInfoStr);
    }

}
