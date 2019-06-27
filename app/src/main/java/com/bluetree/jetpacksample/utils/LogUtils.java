package com.bluetree.jetpacksample.utils;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {
    private final static String TAG = "Logutils";
    public static void i(Object from,String msg) {
        if(msg == null) return;
        Log.i(from == null||from.getClass() == null|| TextUtils.isEmpty(from.getClass().getSimpleName()) ?
                "LogUtils":from.getClass().getSimpleName(), msg);
    }
    public static void e(Object from,String msg) {
        if(msg == null) return;
        Log.e(from == null?"LogUtils":from.getClass().getSimpleName(), msg);
    }
}
