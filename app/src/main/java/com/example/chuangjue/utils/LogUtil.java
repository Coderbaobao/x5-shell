package com.example.chuangjue.utils;

import android.util.Log;

public class LogUtil {
    private static String TAG = "打印日志";

    public static void i(Object object) {
        Log.i(TAG, object + "");
    }
}
