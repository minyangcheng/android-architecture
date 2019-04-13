package com.min.common.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.min.common.widget.R;
import com.min.common.widget.StateLayout;
import com.min.common.widget.divider.DividerGridItemDecoration;
import com.min.common.widget.divider.DividerItemDecoration;
import com.min.common.widget.recyclerview.HFRecyclerViewAdapter;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class RefreshLoaderView extends FrameLayout {

    private Context mContext;

    private StateLayout mStateView;
    private SwipeRefreshLayout mRefreshView;
    private RecyclerView mListRv;

    private int mDividerColor;
    private int mDividerHeight;

    public RefreshLoaderView(Context context) {
        this(context, null);
    }

    public RefreshLoaderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RefreshLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RefreshLoaderView, defStyleAttr, 0);
        mDividerColor = a.getColor(R.styleable.RefreshLoaderView_dividerColor
                , mContext.getResources().getColor(R.color.dividerColor));
        mDividerHeight = a.getDimensionPixelSize(R.styleable.RefreshLoaderView_dividerHeight
                , mContext.getResources().getDimensionPixelSize(R.dimen.divider_height));
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_refresh_loader, this, true);
        mStateView = (StateLayout) findViewById(R.id.view_state);
        mRefreshView = (SwipeRefreshLayout) findViewById(R.id.view_refresh);
        mListRv = (RecyclerView) findViewById(R.id.rv_list);
    }

    public void initWithLinearLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mListRv.setLayoutManager(layoutManager);

        if (mDividerHeight > 0) {
            DividerItemDecoration decoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, mDividerColor, mDividerHeight);
            mListRv.addItemDecoration(decoration);
        }
    }

    public void initWithGridLayout(int spanCount) {
        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, spanCount);
        mListRv.setLayoutManager(layoutManager);

        if (mDividerHeight > 0) {
            DividerGridItemDecoration decoration = new DividerGridItemDecoration(mDividerColor, mDividerHeight);
            mListRv.addItemDecoration(decoration);
        }

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                RecyclerView.Adapter adapter = mListRv.getAdapter();
                if (adapter != null && adapter instanceof HFRecyclerViewAdapter) {
                    HFRecyclerViewAdapter dataAdapter = (HFRecyclerViewAdapter) adapter;
                    if ((dataAdapter.hasHeader() && dataAdapter.isHeader(position)) ||
                            dataAdapter.hasFooter() && dataAdapter.isFooter(position))
                        return layoutManager.getSpanCount();
                }
                return 1;
            }
        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mListRv.setLayoutManager(layoutManager);
    }

    public StateLayout getStateLayout() {
        return mStateView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mRefreshView;
    }

    public RecyclerView getRecyclerView() {
        return mListRv;
    }

}
