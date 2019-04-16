package com.za.cs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.min.core.base.BaseActivity;
import com.min.core.helper.PermissionHelper;
import com.za.cs.R;
import com.za.cs.data.DataManager;

/**
 * Created by minyangcheng on 2019/4/16
 */
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionHelper.requestAppPermission(new PermissionHelper.SimplePermissionCallback() {
            @Override
            public void grantSuccess() {
                init();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void init() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toDistPage();
            }
        }, 2500);
    }

    private void toDistPage() {
        if (DataManager.getPreferencesHelper().getHasLogin()) {
            toMain();
        } else {
            toLogin();
        }
    }

    private void toLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toMain() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
