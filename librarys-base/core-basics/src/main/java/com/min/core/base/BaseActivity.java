package com.min.core.base;

import android.content.Context;
import android.os.Bundle;

import com.min.common.widget.HudDialog;
import com.min.core.helper.inject.ViewInject;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by minyangcheng on 2016/9/27.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private HudDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            ViewInject.inject(this);
        }
    }

    protected Context getContext() {
        return this;
    }

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void showHudDialog(boolean canCancel) {
        if (mProgressDialog == null) {
            mProgressDialog = HudDialog.createProgressHud(getContext(), null, canCancel, null);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void showHudDialog() {
        showHudDialog(true);
    }

    protected void hideHudDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
