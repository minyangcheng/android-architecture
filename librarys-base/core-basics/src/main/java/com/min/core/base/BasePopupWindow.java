package com.min.core.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.min.core.helper.inject.ViewInject;

/**
 * popupwindow的根布局上的layout_width和layout_height都无效，最终大小需要通过构造函数
 * this(context, Kit.dip2px(context,300), ViewGroup.LayoutParams.WRAP_CONTENT)指定
 * <p>
 * 默认大小为宽高都为wrap_content
 */
public abstract class BasePopupWindow extends PopupWindow {

    protected String tag;

    protected Context mContext;

    private View mRootView;

    private OnDismissListener mListener;

    public BasePopupWindow(Context context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BasePopupWindow(Context context, int width, int height) {
        super(context);
        tag = this.getClass().getSimpleName();
        mContext = context;
        setWidth(width);
        setHeight(height);
        init();
    }

    private void init() {
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mRootView = onCreateView();
        if (mRootView != null) {
            onViewCreate(mRootView);
            setContentView(mRootView);
            setDim(0.6f);
        }
    }

    private void setDim(float alpha) {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (!activity.isFinishing()) {
                Window window = activity.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.alpha = alpha;
                window.setAttributes(layoutParams);
            }
        }
    }

    protected View onCreateView() {
        View view = null;
        if (getLayoutId() > 0) {
            view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
            ViewInject.inject(this, view);
            super.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    setDim(1.0f);
                    if (mListener != null) {
                        mListener.onDismiss();
                    }
                }
            });
        }
        return view;
    }

    protected abstract int getLayoutId();

    protected void onViewCreate(View view) {

    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mListener = onDismissListener;
    }

}
