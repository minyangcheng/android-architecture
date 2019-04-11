package com.min.location;

import com.baidu.location.BDLocation;

public class LocationInfo {

    public String country;
    public String countryCode;
    public String province;
    public String city;
    public String cityCode;
    public String district;
    public String street;
    public String streetNumber;
    public String address;
    public double latitude;
    public double longitude;

    public LocationInfo() {
    }

    public LocationInfo(BDLocation bdLocation) {
        this.country = bdLocation.getCountry();
        this.countryCode = bdLocation.getCountryCode();
        this.province = bdLocation.getProvince();
        this.city = bdLocation.getCity();
        this.cityCode = bdLocation.getCityCode();
        this.district = bdLocation.getDistrict();
        this.street = bdLocation.getStreet();
        this.streetNumber = bdLocation.getStreetNumber();
        this.address = bdLocation.getAddress().address;
        this.latitude = bdLocation.getLatitude();
        this.longitude = bdLocation.getLongitude();
    }
}
