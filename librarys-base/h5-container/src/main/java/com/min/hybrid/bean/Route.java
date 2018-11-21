package com.min.hybrid.bean;

import android.content.pm.ActivityInfo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class Route implements Serializable {

    public boolean showNavigationBar = true;

    public boolean showBackBtn = true;

    public String title;

    public int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public String pageUri;

    public JSONObject paramData;

    public Route() {
    }

    public boolean isShowNavigationBar() {
        return showNavigationBar;
    }

    public Route setShowNavigationBar(boolean showNavigationBar) {
        this.showNavigationBar = showNavigationBar;
        return this;
    }

    public boolean isShowBackBtn() {
        return showBackBtn;
    }

    public Route setShowBackBtn(boolean showBackBtn) {
        this.showBackBtn = showBackBtn;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Route setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public Route setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
        return this;
    }

    public String getPageUri() {
        return pageUri;
    }

    public Route setPageUri(String pageUri) {
        this.pageUri = pageUri;
        return this;
    }

    public JSONObject getParamData() {
        return paramData;
    }

    public Route setParamData(JSONObject paramData) {
        this.paramData = paramData;
        return this;
    }

}
