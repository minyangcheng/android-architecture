package com.min.location;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by minyangcheng on 2019/4/11.
 */

public class LocationSdkClient {

    private static LocationSdkClient locationInfo;

    private Context mContext;
    private LocationClient mLocationClient = null;

    private BDLocationListener mBDListener = new MyLocationListener();
    private OnLocationSdkListener mLocationSdkListener;

    public static LocationSdkClient getInstance(Context context) {
        if (locationInfo == null) {
            synchronized (LocationSdkClient.class) {
                if (locationInfo == null) {
                    locationInfo = new LocationSdkClient(context);
                }
            }
        }
        return locationInfo;
    }

    private LocationSdkClient(Context context) {
        mContext = context.getApplicationContext();
        initLocation();
        mLocationClient.registerLocationListener(mBDListener);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        int span = 10000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setEnableSimulateGps(true);
        mLocationClient.setLocOption(option);
    }

    public void startLocation(OnLocationSdkListener listener) {
        if (listener == null) {
            return;
        }
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        this.mLocationSdkListener = listener;
        if (mLocationSdkListener != null) {
            mLocationSdkListener.onStart();
        }
    }

    private void stopLocation() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        mLocationSdkListener = null;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LocationInfo locationInfo = new LocationInfo(location);
            if (mLocationSdkListener != null) {
                mLocationSdkListener.onReceiveLocation(locationInfo);
            }
            stopLocation();
        }

    }


}
