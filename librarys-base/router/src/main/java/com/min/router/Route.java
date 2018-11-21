package com.min.router;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.floo.Floo;
import me.drakeet.floo.Navigation;

/**
 * Created by minych on 18-11-21.
 */

public class Route {

    private String url;
    private Bundle bundle;
    private Map<String, String> queries;

    public Route(String url) {
        this.url = url;
        this.bundle = new Bundle();
        this.queries = new HashMap<>();
    }

    public Route(String url, Bundle bundle, Map<String, String> queries) {
        this.url = url;
        this.bundle = bundle;
        this.queries = queries;
    }

    public void start() {
        Navigation navigation = Floo.navigation(GlobalRouter.getInstance().getContext(), url);
        if (queries != null) {
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                navigation.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        if (bundle != null) {
            navigation.putExtras(bundle);
        }
        navigation.start();
    }

    public Route putExtras(@NonNull Bundle bundle) {
        bundle.putAll(bundle);
        return this;
    }


    public Route appendQueryParameter(@NonNull String key, @NonNull String value) {
        queries.put(key, value);
        return this;
    }

    public Route putExtra(@NonNull String key, int value) {
        bundle.putInt(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, long value) {
        bundle.putLong(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, short value) {
        bundle.putShort(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, @Nullable String value) {
        bundle.putString(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, @Nullable CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }


    public Route putExtra(@NonNull String key, @Nullable Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    public Route putExtra(@NonNull String key, @Nullable Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public Route putExtras(@NonNull Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return putExtras(extras);
        }
        return this;
    }

    public Route putIntegerArrayListExtra(@NonNull String name, @NonNull ArrayList<Integer> value) {
        bundle.putIntegerArrayList(name, value);
        return this;
    }

    public Route putStringArrayListExtra(@NonNull String name, @NonNull ArrayList<String> value) {
        bundle.putStringArrayList(name, value);
        return this;
    }

    public Route putCharSequenceArrayListExtra(@NonNull String name, @NonNull ArrayList<CharSequence> value) {
        bundle.putCharSequenceArrayList(name, value);
        return this;
    }

    public Route putParcelableArrayListExtra(@NonNull String name, @NonNull ArrayList<? extends Parcelable> value) {
        bundle.putParcelableArrayList(name, value);
        return this;
    }

}
