package com.min.core.helper.inject;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;

import com.min.core.helper.inject.annotation.BindView;
import com.min.core.helper.inject.annotation.OnClick;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ViewInject {

    private static final String TAG = ViewInject.class.getSimpleName();

    private ViewInject() {
    }

    public static void inject(Activity activity) {
        injectObject(activity, new ViewFinder(activity));
    }

    public static void inject(Dialog dialog) {
        injectObject(dialog, new ViewFinder(dialog));
    }

    public static void inject(Object target, View view) {
        injectObject(target, new ViewFinder(view));
    }

    private static void injectObject(Object target, ViewFinder finder) {
        try {
            handleFields(target, finder);
            handleMethods(target, finder);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException(e);
        }
    }

    private static void handleMethods(Object target, ViewFinder finder) throws Exception {
        Class targetClass = target.getClass();
        Method[] methods = targetClass.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return;
        }
        OnClick onClick;
        int modifier;
        for (Method method : methods) {
            modifier = method.getModifiers();
            if (Modifier.isPublic(modifier) || Modifier.isPrivate(modifier) || Modifier.isStatic(modifier)) {
                continue;
            }
            method.setAccessible(true);
            onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] ids = onClick.value();
                View[] views = new View[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    views[i] = finder.findViewById(ids[i]);
                    if (views[i] != null) {
                        views[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (method.getTypeParameters().length == 1) {
                                        method.invoke(target, v);
                                    } else if (method.getTypeParameters().length == 0) {
                                        method.invoke(target);
                                    } else {
                                        throw new RuntimeException("ViewInject: can not find method to click");
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, Log.getStackTraceString(e));
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    } else {
                        throw new Exception("ViewInject: can not findView bindView.value()");
                    }
                }
            }
        }
    }

    private static void handleFields(Object target, ViewFinder finder) throws Exception {
        Class targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return;
        }
        BindView bindView;
        int modifier;
        for (Field field : fields) {
            modifier = field.getModifiers();
            if (Modifier.isPublic(modifier) || Modifier.isPrivate(modifier) || Modifier.isStatic(modifier)) {
                continue;
            }
            field.setAccessible(true);
            bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                View view = finder.findViewById(bindView.value());
                if (view != null) {
                    field.set(target, view);
                } else {
                    throw new Exception("ViewInject: can not findView bindView.value()");
                }
            }
        }
    }

}

