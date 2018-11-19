package com.min.hybrid.webview.bridge;

import android.view.View;

import com.min.hybrid.webview.LongCallbackHandler;

/**
 * Created by minych on 18-11-9.
 */

public interface IWebViewHandler {

    LongCallbackHandler getLongCallbackHandler();

    void refresh();

    void showHudDialog(String message, boolean cancelable);

    void hideHudDialog();

    void setNBVisibility(boolean visible);

    void setNBBackBtnVisibility(boolean visible);

    void setNBTitle(String title, String subTitle);

    void setNBTitleClickable(boolean clickable, int arrow);

    void setNBLeftBtn(String imageUrl, String text);

    void hideNBLeftBtn();

    void setNBRightBtn(int which, String imageUrl, String text);

    void hideNBRightBtn(int which);

    View getNBRoot();

    void handleError(String description, String failingUrl);

    void handleFinish(String url);

    void handleProgressChanged(int newProgress);
}
