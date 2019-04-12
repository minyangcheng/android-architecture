package com.min.common.util;

import java.lang.reflect.Field;

public class BuildConfigUtils {

    public static Object getBuildConfigValue(String fieldName) {
        try {
            Class<?> clazz = Class.forName(AppUtils.getAppPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getConfigStr(String configStr) {
        Object result = getBuildConfigValue(configStr);
        return result == null ? null : (String) result;
    }

}
