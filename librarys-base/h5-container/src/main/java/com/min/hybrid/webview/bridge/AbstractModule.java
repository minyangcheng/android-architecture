package com.min.hybrid.webview.bridge;

import android.content.Context;
import android.content.Intent;

public abstract class AbstractModule {

    protected Context mContext;

    public AbstractModule(Context context) {
        this.mContext = context;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

}
