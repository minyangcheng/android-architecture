package com.min.sample.data.model;

public class MainItem {

    public String title;
    public Class destClass;
    public String methodName;

    public MainItem(String title, Class destClass) {
        this.title = title;
        this.destClass = destClass;
    }

    public MainItem(String title, String methodName) {
        this.title = title;
        this.methodName = methodName;
    }

}
