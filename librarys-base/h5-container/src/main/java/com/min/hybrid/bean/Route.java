package com.min.hybrid.bean;

import android.content.pm.ActivityInfo;

import java.io.Serializable;

public class Route implements Serializable {

    public String pageUrl;

    /**
     * -1:不加载导航栏
     * 1：默认类型
     * 2：搜索导航栏
     */
    public int pageStyle = 1;

    /**
     * 0：横屏
     * 1：竖屏（默认）
     * 2:跟随用户设置
     */
    public int orientation = 1;

    public String title;

    /**
     * 默认显示返回键
     */
    public boolean showBackBtn = true;

    public Route(){
    }

    public Route(String pageUrl) {
        this.pageUrl = pageUrl;
    }

}
