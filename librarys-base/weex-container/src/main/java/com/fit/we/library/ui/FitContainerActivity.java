package com.fit.we.library.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.fit.we.library.FitConstants;
import com.fit.we.library.R;
import com.fit.we.library.bean.Route;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.extend.weex.LongCallbackHandler;
import com.fit.we.library.extend.weex.WeexProxy;
import com.fit.we.library.widget.HudDialog;
import com.min.common.widget.TitleBar;

import java.util.Set;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class FitContainerActivity extends AppCompatActivity {

    private WeexProxy mWeexProxy;
    private Route mRoute;

    private FrameLayout mContainerView;
    private TitleBar mTitleBar;

    private HudDialog mHudDialog;

    public static void startActivity(Context context, Route routeInfo) {
        Intent intent = new Intent(context, FitContainerActivity.class);
        intent.putExtra(FitConstants.KEY_ROUTE, routeInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_container);
        getDataFromIntent();
        mContainerView = (FrameLayout) findViewById(R.id.view_container);
        mTitleBar = (TitleBar) findViewById(R.id.view_nb);
        initView();
        mWeexProxy = new WeexProxy(this, mRoute, mWeexHandler);
        mWeexProxy.setDebugMode(mTitleBar);
        mWeexProxy.onCreate(mContainerView);
    }

    private void initView() {
        mTitleBar.setTitleBarListener(new TitleBar.TitleBarListener() {
            @Override
            public void onClickBack() {
                mWeexProxy.onBackPressed(LongCallbackHandler.OnClickNbBack);
            }

            @Override
            public void onClickClose() {
                mWeexProxy.onBackPressed(LongCallbackHandler.OnClickNbBack);
            }

            @Override
            public void onClickLeft() {
                mWeexProxy.getLongCallbackHandler().onClickNbLeft();
            }

            @Override
            public void onClickTitle() {
                mWeexProxy.getLongCallbackHandler().onClickNbTitle();
            }

            @Override
            public void onClickRight(View view, int which) {
                mWeexProxy.getLongCallbackHandler().onClickNbRight(which);
            }
        });
        if (!mRoute.isShowBackBtn()) {
            mTitleBar.setBackVisibility(false);
        }
        if (mRoute.getScreenOrientation() >= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && mRoute.getScreenOrientation() <= ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
            setRequestedOrientation(mRoute.getScreenOrientation());
        }
        if (!TextUtils.isEmpty(mRoute.getTitle())) {
            mTitleBar.setTitle(mRoute.getTitle());
        }
        if (!mRoute.isShowNavigationBar()) {
            mTitleBar.setVisibility(View.GONE);
        }
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra(FitConstants.KEY_ROUTE)) {
            mRoute = (Route) getIntent().getSerializableExtra(FitConstants.KEY_ROUTE);
        }
        if (mRoute == null) {
            Uri uri = getIntent().getData();
            if (uri != null) {
                mRoute = new Route();
                mRoute.pageUri = "fit://" + uri.getPath();

                String showNavigationBar = uri.getQueryParameter("showNavigationBar");
                if (!TextUtils.isEmpty(showNavigationBar)) {
                    mRoute.showNavigationBar = Boolean.valueOf(showNavigationBar);
                }

                String showBackBtn = uri.getQueryParameter("showBackBtn");
                if (!TextUtils.isEmpty(showBackBtn)) {
                    mRoute.showBackBtn = Boolean.valueOf(showBackBtn);
                }

                String screenOrientation = uri.getQueryParameter("screenOrientation");
                if (!TextUtils.isEmpty(screenOrientation)) {
                    mRoute.screenOrientation = Integer.valueOf(screenOrientation);
                }

                mRoute.title = uri.getQueryParameter("title");

                JSONObject paramsData = new JSONObject();
                Set<String> keys = uri.getQueryParameterNames();
                for (String s : keys) {
                    if (s.equals("showNavigationBar") || s.equals("showBackBtn") || s.equals("screenOrientation") || s.equals("title")) {
                        continue;
                    }
                    paramsData.put(s, uri.getQueryParameter(s));
                }
                mRoute.paramsData = paramsData;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeexProxy.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWeexProxy.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWeexProxy.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWeexProxy.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeexProxy.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWeexProxy.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWeexProxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        mWeexProxy.onBackPressed(LongCallbackHandler.OnClickSysBack);
    }

    private IWeexHandler mWeexHandler = new IWeexHandler() {

        @Override
        public LongCallbackHandler getLongCallbackHandler() {
            return mWeexProxy.getLongCallbackHandler();
        }

        @Override
        public void refresh() {
            mWeexProxy.refresh();
        }

        @Override
        public void showHudDialog(String message, boolean cancelable) {
            if (mHudDialog == null) {
                mHudDialog = HudDialog.createProgressHud(FitContainerActivity.this);
            }
            mHudDialog.setCancelable(cancelable);
            mHudDialog.setMessage(message);
            if (!mHudDialog.isShowing()) {
                mHudDialog.show();
            }
        }

        @Override
        public void hideHudDialog() {
            if (mHudDialog != null && mHudDialog.isShowing()) {
                mHudDialog.dismiss();
            }
        }

        @Override
        public void setTitleBarVisibility(boolean visible) {
            mTitleBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setTitleBarBackVisibility(boolean visible) {
            mTitleBar.setBackVisibility(visible);
        }

        @Override
        public void setTitleBarCloseVisibility(boolean visible) {
            mTitleBar.setCloseVisibility(visible);
        }

        @Override
        public void setTitleBarLeftBtn(String imageUrl, String text) {
            mTitleBar.setLeftBtn(imageUrl, text);
        }

        @Override
        public void hideTitleBarLeftButton() {
            mTitleBar.hideLeftButton();
        }

        @Override
        public void setTitleBarRightBtn(int which, String imageUrl, String text) {
            mTitleBar.setRightBtn(which, imageUrl, text);
        }

        @Override
        public void hideTitleBarRightButtons() {
            mTitleBar.hideRightButtons();
        }

        @Override
        public void hideTitleBarRightButton(int which) {
            mTitleBar.hideRightButton(which);
        }

        @Override
        public void setTitle(String title) {
            mTitleBar.setTitle(title);
        }

        @Override
        public void setSubTitle(String subTitle) {
            mTitleBar.setSubTitle(subTitle);
        }

    };

}
