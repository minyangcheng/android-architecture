package com.min.sample.ui.dialog;

import android.content.Context;
import android.view.View;

import com.min.core.base.BasePopupWindow;
import com.min.sample.R;

public class CarPopWin extends BasePopupWindow {

    public CarPopWin(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.popwind_car;
    }

    @Override
    protected void onViewCreate(View view) {
        super.onViewCreate(view);
    }
}
