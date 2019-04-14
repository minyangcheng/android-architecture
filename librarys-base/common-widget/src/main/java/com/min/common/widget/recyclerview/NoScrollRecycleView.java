package com.min.common.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by minyangcheng on 2017/6/7.
 */

public class NoScrollRecycleView extends RecyclerView {

    public NoScrollRecycleView(Context context) {
        super(context);
    }

    public NoScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();

        setNestedScrollingEnabled(false);

    }

}
