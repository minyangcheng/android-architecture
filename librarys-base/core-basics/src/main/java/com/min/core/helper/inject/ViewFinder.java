package com.min.core.helper.inject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

class ViewFinder {

    private View view;
    private Activity activity;
    private Dialog dialog;

    public ViewFinder(View view) {
        this.view = view;
    }

    public ViewFinder(Activity activity) {
        this.activity = activity;
    }

    public ViewFinder(Dialog dialog) {
        this.dialog = dialog;
    }

    public View findViewById(int id) {
        if (view != null) {
            return view.findViewById(id);
        } else if (activity != null) {
            return activity.findViewById(id);
        } else if (dialog != null) {
            return dialog.findViewById(id);
        }
        return null;
    }

    public Context getContext() {
        if (view != null) return view.getContext();
        if (activity != null) return activity;
        if (dialog != null) return dialog.getContext();
        return null;
    }

}

