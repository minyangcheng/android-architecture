package com.min.hybrid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.min.common.widget.TitleBar;
import com.min.hybrid.bean.HybridEvent;
import com.min.hybrid.bean.Route;
import com.min.hybrid.util.EventUtil;
import com.min.hybrid.util.L;
import com.min.hybrid.webview.HybridWebChromeClient;
import com.min.hybrid.webview.HybridWebView;
import com.min.hybrid.webview.HybridWebViewClient;
import com.min.hybrid.webview.LongCallbackHandler;
import com.min.hybrid.webview.bridge.IWebViewHandler;
import com.min.hybrid.webview.bridge.JSBridge;
import com.min.hybrid.webview.bridge.ModuleInstance;
import com.min.hybrid.widget.HudDialog;
import com.min.hybrid.widget.WebViewProgressBar;

import de.greenrobot.event.Subscribe;

public class H5ContainerActivity extends AppCompatActivity {

    private Route mRoute;

    private TitleBar mTitleBar;
    private WebViewProgressBar mProgressBar;
    private HybridWebView mWebView;

    private View mErrorContentView;
    private View mRetryView;

    private ModuleInstance moduleInstance;
    private LongCallbackHandler mLongCallbackHandler;

    private HudDialog mHudDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_container);
        getRoute(savedInstanceState);
        EventUtil.register(this);
        findView();
        initView();
        setRouteData();

        moduleInstance = new ModuleInstance(this);
        moduleInstance.setWebViewHandler(mWebViewHandler);
    }

    public void getRoute(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(HybridConstants.KEY_ROUTE)) {
            mRoute = (Route) savedInstanceState.getSerializable(HybridConstants.KEY_ROUTE);
        } else if (getIntent().hasExtra(HybridConstants.KEY_ROUTE)) {
            mRoute = (Route) getIntent().getSerializableExtra(HybridConstants.KEY_ROUTE);
        }
        if (mRoute == null) {
            Uri uri = getIntent().getData();
            if (uri != null) {
                String scheme = uri.getQueryParameter("scheme");
                if (TextUtils.isEmpty(scheme)) {
                    scheme = "http";
                }
                String host = uri.getQueryParameter("host");
                String path = uri.getPath();
                String query = uri.getQuery();

                mRoute = new Route();
                if (TextUtils.isEmpty(path)) {
                    mRoute.pageUri = scheme + "://" + host + "?" + query;
                } else {
                    mRoute.pageUri = scheme + "://" + host + "/" + path + "?" + query;
                }

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
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRoute != null) {
            outState.putSerializable(HybridConstants.KEY_ROUTE, mRoute);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        mWebView.onResume();
        mWebView.resumeTimers();
        super.onResume();
        if (mLongCallbackHandler.hasPort(LongCallbackHandler.OnPageResume)) {
            mLongCallbackHandler.onPageResume();
        }
    }

    @Override
    public void onPause() {
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
        if (mLongCallbackHandler.hasPort(LongCallbackHandler.OnPagePause)) {
            mLongCallbackHandler.onPagePause();
        }
    }

    @Override
    public void onDestroy() {
        mWebView.stopLoading();
        try {
            if (mWebView != null) {
                ViewGroup parent = (ViewGroup) mWebView.getParent();
                if (parent != null) {
                    parent.removeView(mWebView);
                }
                mWebView.removeAllViews();
                mWebView.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventUtil.unregister(this);
        if (moduleInstance != null) {
            moduleInstance.destroy();
        }
        super.onDestroy();
    }

    private void findView() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mWebView = (HybridWebView) findViewById(R.id.wv);
        mProgressBar = (WebViewProgressBar) findViewById(R.id.pb);
        mErrorContentView = findViewById(R.id.view_error);
        mRetryView = findViewById(R.id.view_retry);
        mRetryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
                mErrorContentView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initView() {
        mTitleBar.setTitleBarListener(new TitleBar.TitleBarListener() {
            @Override
            public void onClickBack() {
                backPress(LongCallbackHandler.OnClickNbBack);
            }

            @Override
            public void onClickClose() {
                backPress(LongCallbackHandler.OnClickNbBack);
            }

            @Override
            public void onClickLeft() {
                mLongCallbackHandler.onClickNbLeft();
            }

            @Override
            public void onClickTitle() {
                mLongCallbackHandler.onClickNbTitle(0);
            }

            @Override
            public void onClickRight(View view, int which) {
                mLongCallbackHandler.onClickNbRight(which);
            }
        });
        mTitleBar.setBackVisibility(mRoute.showBackBtn);
        initWebView();
    }

    private void initWebView() {
        mLongCallbackHandler = new LongCallbackHandler(mWebView);
        HybridWebViewClient webViewClient = new HybridWebViewClient();
        mWebView.setWebViewClient(webViewClient);
        HybridWebChromeClient chromeClient = new HybridWebChromeClient(this);
        mWebView.setWebChromeClient(chromeClient);
    }


    @SuppressLint("WrongConstant")
    private void setRouteData() {
        if (mRoute != null) {
            if (mRoute.screenOrientation >= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && mRoute.screenOrientation <= ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                setRequestedOrientation(mRoute.screenOrientation);
            }
            if (!TextUtils.isEmpty(mRoute.title)) {
                mTitleBar.setTitle(mRoute.title);
            }
            if (!mRoute.showNavigationBar) {
                mTitleBar.setVisibility(View.GONE);
            }
        }
        loadUrl();
    }

    private void loadUrl() {
        if (mRoute == null || TextUtils.isEmpty(mRoute.pageUri)) {
            Toast.makeText(this, "请求参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        L.d(HybridConstants.TAG, "loadUrl source url: %s", getIntent().getData().toString());
        L.d(HybridConstants.TAG, "loadUrl dist url: %s ", mRoute.pageUri);
        mWebView.loadUrl(mRoute.pageUri);
    }

    public void backPress(String eventType) {
        if (mLongCallbackHandler.hasPort(eventType)) {
            if (eventType == LongCallbackHandler.OnClickNbBack) {
                mLongCallbackHandler.onClickNbBack();
            } else if (eventType == LongCallbackHandler.OnClickBack) {
                mLongCallbackHandler.onClickBack();
            }
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        backPress(LongCallbackHandler.OnClickBack);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (moduleInstance != null) {
            moduleInstance.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe
    public void onEvent(HybridEvent event) {
        JSBridge.executeJsByEvent(mWebView, event.type, event.data);
    }

    private IWebViewHandler mWebViewHandler = new IWebViewHandler() {

        @Override
        public LongCallbackHandler getLongCallbackHandler() {
            return mLongCallbackHandler;
        }

        @Override
        public void refresh() {
            mWebView.clearHistory();
            loadUrl();
        }

        @Override
        public void showHudDialog(String message, boolean cancelable) {
            if (mHudDialog == null) {
                mHudDialog = HudDialog.createProgressHud(H5ContainerActivity.this);
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
        public void handleError(String description, String failingUrl) {
            L.d(HybridConstants.TAG, "handleError description=%s , failingUrl=%s", description, failingUrl);
            mErrorContentView.setVisibility(View.VISIBLE);
        }

        @Override
        public void handleFinish(String url) {
            L.d(HybridConstants.TAG, "handleFinish", url);
        }

        @Override
        public void handleProgressChanged(int newProgress) {
            L.d(HybridConstants.TAG, "handleProgressChanged newProgress=%s", newProgress);
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.INVISIBLE);
            } else {
                if (mProgressBar.getVisibility() == View.INVISIBLE)
                    mProgressBar.setVisibility(View.VISIBLE);
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
