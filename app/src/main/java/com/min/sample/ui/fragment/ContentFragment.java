package com.min.sample.ui.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.min.core.base.BaseFragment;
import com.min.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentFragment extends BaseFragment {

    private static final String ARG_CONTENT = "contentArg";

    private String content;

    @BindView(R.id.tv_content)
    TextView mContentTv;

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
        ButterKnife.bind(this,view);

        mContentTv.setText(content);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }
}
