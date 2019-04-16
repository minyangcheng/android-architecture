package com.min.core.view.choice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.min.common.util.ToastUtils;
import com.min.common.widget.TitleBar;
import com.min.common.widget.divider.DividerItemDecoration;
import com.min.core.R;
import com.min.core.base.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ChoiceDialog extends BaseDialogFragment {

    private TitleBar mTitleBar;
    private RecyclerView mRv;

    private ChoiceAdapter mAdapter;

    private String mTitle;
    private List<ChoiceBean> mDataList;
    private boolean mSingleFlag;
    private boolean mAutoDismissFlag = true;
    private boolean mShowArrow;

    private OnSingleSelectResultListener mSingleListener;
    private OnMultipleSelectResultListener mMultipleListener;

    public ChoiceDialog() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseAppTheme);
    }


    public static ChoiceDialog newInstanceForSingle(String title, List<ChoiceBean> dataList, OnSingleSelectResultListener singleListener) {
        return newInstanceForSingle(title, dataList, singleListener, false);
    }

    public static ChoiceDialog newInstanceForSingle(String title, List<ChoiceBean> dataList, OnSingleSelectResultListener singleListener, boolean showArrowFlag) {
        ChoiceDialog fragment = new ChoiceDialog();
        fragment.setTitle(title);
        fragment.setDataList(dataList);
        fragment.setOnSingleSelectResultListener(singleListener);
        fragment.setArrowFlag(showArrowFlag);
        return fragment;
    }

    public static ChoiceDialog newInstanceForMultiple(String title, List<ChoiceBean> dataList, OnMultipleSelectResultListener multipleListener) {
        ChoiceDialog fragment = new ChoiceDialog();
        fragment.setTitle(title);
        fragment.setDataList(dataList);
        fragment.setOnMultipleSelectResultListener(multipleListener);
        return fragment;
    }

    public static ChoiceDialog newInstance(String title, List<ChoiceBean> dataList, OnSingleSelectResultListener singleListener, OnMultipleSelectResultListener multipleListener) {
        ChoiceDialog fragment = new ChoiceDialog();
        fragment.setTitle(title);
        fragment.setDataList(dataList);
        fragment.setOnSingleSelectResultListener(singleListener);
        fragment.setOnMultipleSelectResultListener(multipleListener);
        return fragment;
    }

    public static List<ChoiceBean> getChoiceList(String[] arrStr, int[] arrInt) {
        List<ChoiceBean> dataList = new ArrayList<>();
        if (arrStr != null && arrInt != null) {
            for (int i = 0; i < arrStr.length; i++) {
                dataList.add(new ChoiceBean(arrStr[i], arrInt[i]));
            }
        }
        return dataList;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        mSingleFlag = mSingleListener != null;
        initView();
    }

    private void findView(View view) {
        mTitleBar = view.findViewById(R.id.title_bar);
        mRv = view.findViewById(R.id.rv);
    }

    private void initView() {
        mTitleBar.setTitleBarListener(new TitleBar.TitleBarListener() {
            @Override
            public void onClickBack() {
                dismissAllowingStateLoss();
            }

            @Override
            public void onClickClose() {

            }

            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickTitle() {

            }

            @Override
            public void onClickRight(View view, int which) {
                if (which == 0) {
                    setResultForMultiple();
                }
            }
        });
        mTitleBar.setTitle(mTitle);
        if (!mSingleFlag) {
            mTitleBar.setRightBtn("确定");
        }
        initRv();
    }

    protected void setResultForMultiple() {
        List<ChoiceBean> hasSelectDataList = new ArrayList<>();
        for (ChoiceBean data : mAdapter.getData()) {
            if (data.selectFlag) {
                hasSelectDataList.add(data);
            }
        }
        if (hasSelectDataList.size() == 0) {
            ToastUtils.showShort("请至少选择一个选项");
        } else {
            if (!mSingleFlag && mMultipleListener != null) {
                mMultipleListener.onResult(hasSelectDataList);
            }
            if (mAutoDismissFlag) {
                dismissAllowingStateLoss();
            }
        }
    }

    private void initRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, getContext().getResources().getDrawable(R.drawable.divider));
        mRv.addItemDecoration(decoration);
        mAdapter = new ChoiceAdapter(getContext(), mSingleFlag, mShowArrow);
        mRv.setAdapter(mAdapter);

        mAdapter.setData(mDataList);
        mAdapter.setOnItemClickLitener((view, position) -> {
            if (mSingleFlag) {
                setResultForSingle(mAdapter.getData().get(position));
            } else {
                handleForMultipleClick(mAdapter.getData().get(position));
            }
        });
    }

    protected void setResultForSingle(ChoiceBean data) {
        if (mSingleFlag && mSingleListener != null) {
            mSingleListener.onResult(data);
        }
        if (mAutoDismissFlag) {
            dismissAllowingStateLoss();
        }
    }

    protected void handleForMultipleClick(ChoiceBean data) {
        data.selectFlag = !data.selectFlag;
        mAdapter.notifyDataSetChanged();
    }

    public ChoiceDialog setDataList(List<ChoiceBean> dataList) {
        this.mDataList = dataList;
        return this;
    }

    public ChoiceDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public ChoiceDialog setAutoDismissFlag(boolean autoDismissFlag) {
        mAutoDismissFlag = autoDismissFlag;
        return this;
    }

    public ChoiceDialog setAdapter(ChoiceAdapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public ChoiceAdapter getAdapter() {
        return mAdapter;
    }

    private void setArrowFlag(boolean arrowFlag) {
        this.mShowArrow = arrowFlag;
    }

    public static class ChoiceBean {

        public String statusStr;
        public int status;
        public boolean selectFlag;

        public ChoiceBean() {
        }

        public ChoiceBean(String statusStr, int status) {
            this.statusStr = statusStr;
            this.status = status;
        }

    }

    public ChoiceDialog setOnSingleSelectResultListener(OnSingleSelectResultListener listener) {
        mSingleListener = listener;
        return this;
    }

    public ChoiceDialog setOnMultipleSelectResultListener(OnMultipleSelectResultListener listener) {
        mMultipleListener = listener;
        return this;
    }

    public interface OnSingleSelectResultListener {
        void onResult(ChoiceBean singleResult);
    }

    public interface OnMultipleSelectResultListener {
        void onResult(List<ChoiceBean> multipleResult);
    }

}
