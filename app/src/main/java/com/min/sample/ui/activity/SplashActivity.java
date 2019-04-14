package com.min.sample.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fit.we.library.FitWe;
import com.min.core.base.BaseActivity;
import com.min.core.helper.PermissionHelper;
import com.min.sample.R;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PermissionHelper.requestAppPermission(new PermissionHelper.SimplePermissionCallback() {
            @Override
            public void grantSuccess() {
                init();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long prepareTime = FitWe.getInstance().prepareJsBundle(SplashActivity.this);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toHome();
                    }
                }, 2500 - prepareTime);
            }
        }).start();
    }

    private void toHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
