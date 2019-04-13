package com.min.common.widget;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;

public class Kit {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static ShapeDrawable getDividerDrawable(int color, int height) {
        android.graphics.drawable.ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setIntrinsicHeight(height);
        shapeDrawable.setIntrinsicWidth(height);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

}
