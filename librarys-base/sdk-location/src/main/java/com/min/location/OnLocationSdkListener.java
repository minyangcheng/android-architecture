package com.min.location;

public interface OnLocationSdkListener {

    public void onStart();

    public void onReceiveLocation(LocationInfo locationInfo);

}
