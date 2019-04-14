package com.min.sample.ui.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.min.common.util.LogUtils;
import com.min.common.widget.CellView;
import com.min.core.base.BaseFragment;
import com.min.core.helper.inject.annotation.BindView;
import com.min.core.helper.inject.annotation.OnClick;
import com.min.sample.R;


public class ContentFragment extends BaseFragment {

    private static final String ARG_CONTENT = "contentArg";

    private String content;

    @BindView(R.id.tv_content)
    TextView mContentTv;
    @BindView(R.id.cv_place)
    CellView mPlaceCv;
    @BindView(R.id.cv_idcard)
    CellView mIdcardCv;

    public ContentFragment() {
    }

    public static ContentFragment newInstance(String content) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        fragment.tag=fragment.tag+"_"+content;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = getArguments().getString(ARG_CONTENT);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentTv.setText(content);
        mIdcardCv.setValue(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @OnClick(R.id.cv_place)
    void clickPlaceCell(){
        LogUtils.d(mIdcardCv.getValue());
    }

}
