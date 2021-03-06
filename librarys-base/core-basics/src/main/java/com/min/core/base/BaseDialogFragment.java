package com.min.core.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.min.core.R;
import com.min.core.helper.inject.ViewInject;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

/**
 * 基于Dialog的对话框生命周期是不会随着Activity的，所以优先使用DialogFragment
 */
public abstract class BaseDialogFragment extends RxDialogFragment {

    protected Context mContext;

    public BaseDialogFragment() {
        setStyle(STYLE_NORMAL, R.style.DialogMatch);
    }

    public BaseDialogFragment(int themeResId) {
        setStyle(STYLE_NORMAL, themeResId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() > 0) {
            View view = inflater.inflate(getLayoutId(), container, false);
            ViewInject.inject(this,view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
