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

public class ExampleDialogFragment extends BaseDialogFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_example;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.view_fl)
    void clickView() {
        dismiss();
    }

}