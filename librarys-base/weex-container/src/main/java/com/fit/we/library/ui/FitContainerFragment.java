package com.fit.we.library.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fit.we.library.FitConstants;
import com.fit.we.library.R;
import com.fit.we.library.bean.Route;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.extend.weex.LongCallbackHandler;
import com.fit.we.library.extend.weex.WeexProxy;
import com.fit.we.library.widget.HudDialog;
import com.min.common.widget.TitleBar;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class FitContainerFragment extends Fragment {

    private WeexProxy mWeexProxy;
    private Route mRoute;

    private FrameLayout mContainerView;
    private TitleBar mTitleBar;

    private HudDialog mHudDialog;

    public static FitContainerFragment newInstance(Route routeInfo) {
        FitContainerFragment fragment = new FitContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(FitConstants.KEY_ROUTE, routeInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_container, container, false);
        mContainerView = (FrameLayout) view.findViewById(R.id.view_container);
        mTitleBar = (TitleBar) view.findViewById(R.id.view_nb);
        initView();
        mWeexProxy = new WeexProxy(getActivity(), mRoute, mWeexHandler);
        mWeexProxy.onCreate(mContainerView);
        mWeexProxy.setDebugMode(mContainerView);
        return view;
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
            getActivity().setRequestedOrientation(mRoute.getScreenOrientation());
        }
        if (!TextUtils.isEmpty(mRoute.getTitle())) {
            mTitleBar.setTitle(mRoute.getTitle());
        }
        if (!mRoute.isShowNavigationBar()) {
            mTitleBar.setVisibility(View.GONE);
        }
    }

    private void getDataFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FitConstants.KEY_ROUTE)) {
            mRoute = (Route) bundle.getSerializable(FitConstants.KEY_ROUTE);
        }
        if (mRoute == null || TextUtils.isEmpty(mRoute.getPageUri()) || !mRoute.getPageUri().startsWith("fit://")) {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWeexProxy.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mWeexProxy.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWeexProxy.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWeexProxy.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWeexProxy.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWeexProxy.onActivityResult(requestCode, resultCode, data);
    }

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
                mHudDialog = HudDialog.createProgressHud(getContext());
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
