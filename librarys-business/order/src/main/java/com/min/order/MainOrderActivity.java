package com.min.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.min.core.base.BaseActivity;

public class MainOrderActivity extends BaseActivity {

    TextView mExtrasTv;
    TextView mQueryTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExtrasTv = findViewById(R.id.tv_extras);
        mQueryTv = findViewById(R.id.tv_query);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.getData() != null) {
            Bundle bundle = intent.getExtras();
            mExtrasTv.append("extras:\n\n");
            for (String key : bundle.keySet()) {
                mExtrasTv.append(key + "=" + bundle.get(key) + "\n\n");
            }
            mQueryTv.append("query:\n\n");
            String commpanyName = intent.getData().getQueryParameter("company_name");
            mQueryTv.append("commpanyName=" + commpanyName + "\n\n");
            String userId = intent.getData().getQueryParameter("user_id");
            mQueryTv.append("user_id=" + userId + "\n\n");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

}