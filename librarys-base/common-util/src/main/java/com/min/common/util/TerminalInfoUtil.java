package com.min.common.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;

/**
 * Created by minyangcheng on 2019/4/11.
 */

public class TerminalInfoUtil {

    private static TerminalInfoUtil terminalUtil;

    private Context context;
    private TerminalInfo terminalInfo;

    public static TerminalInfoUtil getInstance(Context context) {
        if (terminalUtil == null) {
            synchronized (TerminalInfoUtil.class) {
                if (terminalUtil == null) {
                    terminalUtil = new TerminalInfoUtil(context.getApplicationContext());
                }
            }
        }
        return terminalUtil;
    }

    private TerminalInfoUtil(Context context) {
        this.context = context;
        terminalInfo = initTerminalInfo();
    }

    public TerminalInfo getTerminalInfo() {
        return terminalInfo;
    }


    private TerminalInfo initTerminalInfo() {
        terminalInfo = new TerminalInfo();
        terminalInfo.serial = Build.SERIAL;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            terminalInfo.imei = PhoneUtils.getIMEI();
            terminalInfo.imsi = PhoneUtils.getIMSI();
        }
        terminalInfo.phoneType = PhoneUtils.getPhoneType();
        terminalInfo.androidId = DeviceUtils.getAndroidID();
        terminalInfo.macAddress = DeviceUtils.getMacAddress();
        terminalInfo.manufacturer = DeviceUtils.getManufacturer();
        terminalInfo.model = DeviceUtils.getModel();
        return terminalInfo;
    }

    public static class TerminalInfo {
        /**
         * sn串号:f337cacc
         */
        public String serial;
        /**
         * imei号:868239026749718
         */
        public String imei;
        /**
         * imsi号:868239026749123(可能为空)
         */
        public String imsi;
        /**
         * 手机制式:0 手机制式未知 1 手机制式为GSM，移动和联通 2 手机制式为CDMA，电信
         */
        public int phoneType;
        /**
         * AndroidID:6121e66758d980b3
         */
        public String androidId;
        /**
         * MAC地址:bc:3a:ea:bf:09:94
         */
        public String macAddress;
        /**
         * 设备厂商:OPPO
         */
        public String manufacturer;
        /**
         * 设备型号:R7c
         */
        public String model;
    }

}
