package com.fit.we.library.extend.weex;

import android.view.View;

/**
 * Created by minych on 18-11-9.
 */

public interface IWeexHandler {

    LongCallbackHandler getLongCallbackHandler();

    void refresh();

    void showHudDialog(String message, boolean cancelable);

    void hideHudDialog();

    void setTitleBarVisibility(boolean visible);

    void setTitleBarBackVisibility(boolean visible);

    void setTitleBarCloseVisibility(boolean visible);

    void setTitleBarLeftBtn(String imageUrl, String text);

    void hideTitleBarLeftButton();

    void setTitleBarRightBtn(int which, String imageUrl, String text);

    void hideTitleBarRightButtons();

    void hideTitleBarRightButton(int which);

    void setTitle(String title);

    void setSubTitle(String subTitle);
}
