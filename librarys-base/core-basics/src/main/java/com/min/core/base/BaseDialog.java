package com.min.core.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.min.core.R;
import com.min.core.helper.inject.ViewInject;

/**
 * Created by minyangcheng on 2017/9/18.
 */

public abstract class BaseDialog extends Dialog {

    private View mRootView;

    public BaseDialog(Context context) {
        this(context, R.style.DialogMatch);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            mRootView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
            onViewCreate(mRootView);
            setContentView(mRootView);
            ViewInject.inject(this);
        }
    }

    protected abstract int getLayoutId();

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    protected void onViewCreate(View view) {
    }

}
