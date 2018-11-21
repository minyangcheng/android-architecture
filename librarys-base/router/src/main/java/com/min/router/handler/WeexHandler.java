package com.min.router.handler;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.drakeet.floo.TargetNotFoundHandler;

/**
 * Created by minych on 18-11-21.
 */

public class WeexHandler implements TargetNotFoundHandler {

    @Override
    public boolean onTargetNotFound(@NonNull Context context, @NonNull Uri uri, @NonNull Bundle bundle, @Nullable Integer intentFlags) {
        String host = uri.getHost();
        if ("weex.com".equals(host)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtras(bundle);
            if (intentFlags != null) {
                intent.setFlags(intentFlags);
            }
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
