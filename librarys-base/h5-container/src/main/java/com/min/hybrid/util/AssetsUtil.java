package com.min.hybrid.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Assets 文件工具类
 */
public class AssetsUtil {

    /**
     * 读取assets 文件
     */
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String s;
            while ((s = bufReader.readLine()) != null) {
                result += s;
            }
            bufReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
