package com.min.sample.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.min.core.base.BaseDialogFragment;
import com.min.sample.R;

import butterknife.OnClick;

/**
 * Created by minyangcheng on 2017/9/19.
 */

public class CarSelectDialogFragment extends BaseDialogFragment {

//    public CarSelectDialogFragment(){
//        super(R.style.AppTheme);  //以页面形式展示
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_car_select;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}