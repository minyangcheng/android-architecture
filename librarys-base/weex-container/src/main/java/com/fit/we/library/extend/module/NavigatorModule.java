package com.fit.we.library.extend.module;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.extend.weex.LongCallbackHandler;
import com.fit.we.library.extend.weex.WeexHandlerManager;
import com.fit.we.library.util.DeviceUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class NavigatorModule extends WXModule {

    /**
     * 隐藏导航栏
     */
    @JSMethod(uiThread = true)
    public void hide(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.setTitleBarVisibility(false);
            successCallback.invoke(null);
        }
    }

    /**
     * 显示导航栏
     */
    @JSMethod(uiThread = true)
    public void show(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.setTitleBarVisibility(true);
            successCallback.invoke(null);
        }
    }

    /**
     * 显示状态栏
     */
    @JSMethod(uiThread = true)
    public void showStatusBar(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        Activity activity = (Activity) mWXSDKInstance.getContext();
        DeviceUtil.setStatusBarVisibility(activity, true);
        successCallback.invoke(null);
    }

    /**
     * 隐藏状态栏
     */
    @JSMethod(uiThread = true)
    public void hideStatusBar(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        Activity activity = (Activity) mWXSDKInstance.getContext();
        DeviceUtil.setStatusBarVisibility(activity, false);
        successCallback.invoke(null);
    }

    /**
     * 隐藏导航栏返回按钮,只能在首页使用
     */
    @JSMethod(uiThread = true)
    public void hideBackBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.setTitleBarBackVisibility(false);
            successCallback.invoke(null);
        }
    }

    /**
     * 监听系统返回按钮
     */
    @JSMethod(uiThread = true)
    public void hookSysBack(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.getLongCallbackHandler().addJSCallback(LongCallbackHandler.OnClickSysBack, successCallback);
        }
    }

    /**
     * 监听导航栏返回按钮
     */
    @JSMethod(uiThread = true)
    public void hookBackBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.getLongCallbackHandler().addJSCallback(LongCallbackHandler.OnClickNbBack, successCallback);
        }
    }

    /**
     * 设置标题
     * title:   标题
     * subTitle:副标题
     * clickable：是否可点击，1：是，并且显示小箭头 其他：否
     * direction：箭头方向 top：向上 bottom：向下
     */
    @JSMethod(uiThread = true)
    public void setTitle(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            String title = params.getString("title");
            String subTitle = params.getString("subTitle");
            boolean clickable = "1".equals(params.getIntValue("clickable"));
            weexHandler.setTitle(title);
            if (TextUtils.isEmpty(subTitle)) {
                weexHandler.setSubTitle(subTitle);
            }
            if (clickable) {
                weexHandler.getLongCallbackHandler().addJSCallback(LongCallbackHandler.OnClickNbTitle, successCallback);
            } else {
                weexHandler.getLongCallbackHandler().removeJSCallback(LongCallbackHandler.OnClickNbTitle);
            }
        }
    }

    /**
     * 设置导航栏最右侧按钮
     * isShow：是否显示，0：隐藏 其他：显示
     * text：文字按钮
     * imageUrl:图片按钮，格式为url地址,优先级高
     */
    @JSMethod(uiThread = true)
    public void setRightBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            int which = params.getIntValue("which");
            boolean isShow = !"0".equals(params.getString("isShow"));
            String text = params.getString("text");
            String imageUrl = params.getString("imageUrl");
            if (isShow) {
                weexHandler.setTitleBarRightBtn(which, imageUrl, text);
                weexHandler.getLongCallbackHandler().addJSCallback(LongCallbackHandler.OnClickNbRight + which, successCallback);
            } else {
                weexHandler.hideTitleBarRightButton(which);
                weexHandler.getLongCallbackHandler().removeJSCallback(LongCallbackHandler.OnClickNbRight + which);
            }
        }
    }

    /**
     * 设置导航栏左侧按钮
     * isShow：是否显示，0：隐藏 其他：显示
     * text：文字按钮
     * imageUrl:图片按钮，格式为url地址,优先级高
     */
    @JSMethod(uiThread = true)
    public void setLeftBtn(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            boolean isShow = !"0".equals(params.getString("isShow"));
            String text = params.getString("text");
            String imageUrl = params.getString("imageUrl");
            if (isShow) {
                weexHandler.setTitleBarLeftBtn(imageUrl, text);
                weexHandler.getLongCallbackHandler().addJSCallback(LongCallbackHandler.OnClickNbLeft, successCallback);
            } else {
                weexHandler.hideTitleBarLeftButton();
                weexHandler.getLongCallbackHandler().removeJSCallback(LongCallbackHandler.OnClickNbLeft);
            }
        }
    }

}
