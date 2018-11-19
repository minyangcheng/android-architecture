package com.min.hybrid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.min.hybrid.bean.HybridEvent;
import com.min.hybrid.bean.Route;
import com.min.hybrid.library.R;
import com.min.hybrid.util.EventUtil;
import com.min.hybrid.util.HybridUtil;
import com.min.hybrid.util.L;
import com.min.hybrid.webview.HybridWebChromeClient;
import com.min.hybrid.webview.HybridWebView;
import com.min.hybrid.webview.HybridWebViewClient;
import com.min.hybrid.webview.LongCallbackHandler;
import com.min.hybrid.webview.bridge.IWebViewHandler;
import com.min.hybrid.webview.bridge.JSBridge;
import com.min.hybrid.webview.bridge.ModuleInstance;
import com.min.hybrid.widget.NavigationBar;
import com.min.hybrid.widget.WebViewProgressBar;

import de.greenrobot.event.Subscribe;

public class H5ContainerActivity extends AppCompatActivity {

    private Route mRoute;

    private NavigationBar mNavigationBar;
    private WebViewProgressBar mProgressBar;
    private HybridWebView mWebView;

    private View mErrorContentView;
    private View mRetryView;

    private ModuleInstance moduleInstance;
    private LongCallbackHandler mLongCallbackHandler;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, H5ContainerActivity.class);
        Route data = new Route(url);
        intent.putExtra(HybridConstants.KEY_ROUTE, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_container);
        getDataFromIntent(savedInstanceState);
        EventUtil.register(this);
        findView();
        initView();
        setRouteData();

        moduleInstance = new ModuleInstance(this);
        moduleInstance.setWebViewHandler(mWebViewHandler);
    }

    public void getDataFromIntent(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(HybridConstants.KEY_ROUTE)) {
            mRoute = (Route) savedInstanceState.getSerializable(HybridConstants.KEY_ROUTE);
        } else if (getIntent().hasExtra(HybridConstants.KEY_ROUTE)) {
            mRoute = (Route) getIntent().getSerializableExtra(HybridConstants.KEY_ROUTE);
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
        mNavigationBar = (NavigationBar) findViewById(R.id.view_nb);
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
        mNavigationBar.setOnNavigationBarListener(new NavigationBar.INbOnClick() {
            @Override
            public void onNbBack() {
                backPress(LongCallbackHandler.OnClickNbBack);
            }

            @Override
            public void onNbLeft(View view) {
                if (view.getTag() != null && "close".equals(view.getTag().toString())) {
                    onNbBack();
                } else {
                    mLongCallbackHandler.onClickNbLeft();
                }
            }

            @Override
            public void onNbRight(View view, int which) {
                mLongCallbackHandler.onClickNbRight(which);
            }

            @Override
            public void onNbTitle(View view) {
                mLongCallbackHandler.onClickNbTitle(0);
            }
        });
        mNavigationBar.setColorFilter(HybridUtil.getColorPrimary(this));
        if (!mRoute.showBackBtn) {
            mNavigationBar.hideNbBack();
        }
        initWebView();
    }

    private void initWebView() {
        mLongCallbackHandler = new LongCallbackHandler(mWebView);
        HybridWebViewClient webViewClient = new HybridWebViewClient();
        mWebView.setWebViewClient(webViewClient);
        HybridWebChromeClient chromeClient = new HybridWebChromeClient(this);
        mWebView.setWebChromeClient(chromeClient);
    }


    private void setRouteData() {
        if (mRoute != null) {
            if (mRoute.orientation >= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && mRoute.orientation <= ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                setRequestedOrientation(mRoute.orientation);
            }
            if (!TextUtils.isEmpty(mRoute.title)) {
                mNavigationBar.setNbTitle(mRoute.title);
            }
            if (mRoute.pageStyle == -1) {
                mNavigationBar.hide();
            }
        }
        loadUrl();
    }

    private void loadUrl() {
        if (mRoute == null || TextUtils.isEmpty(mRoute.pageUrl)) {
            Toast.makeText(this, "请求参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        L.d(HybridConstants.TAG, "loadUrl=%s", mRoute.pageUrl);
        mWebView.loadUrl(mRoute.pageUrl);
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
            return null;
        }

        @Override
        public void refresh() {

        }

        @Override
        public void showHudDialog(String message, boolean cancelable) {

        }

        @Override
        public void hideHudDialog() {

        }

        @Override
        public void setNBVisibility(boolean visible) {

        }

        @Override
        public void setNBBackBtnVisibility(boolean visible) {

        }

        @Override
        public void setNBTitle(String title, String subTitle) {

        }

        @Override
        public void setNBTitleClickable(boolean clickable, int arrow) {

        }

        @Override
        public void setNBLeftBtn(String imageUrl, String text) {

        }

        @Override
        public void hideNBLeftBtn() {

        }

        @Override
        public void setNBRightBtn(int which, String imageUrl, String text) {

        }

        @Override
        public void hideNBRightBtn(int which) {

        }

        @Override
        public View getNBRoot() {
            return null;
        }

        @Override
        public void handleError(String description, String failingUrl) {

        }

        @Override
        public void handleFinish(String url) {

        }

        @Override
        public void handleProgressChanged(int newProgress) {

        }
    };

}
