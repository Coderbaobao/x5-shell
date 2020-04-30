package com.example.chuangjue.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;

import com.example.chuangjue.utils.AidlUtil;


/**
 * js通信接口
 */
public class MyJavascriptImpl {
    private Context context;


    public MyJavascriptImpl(Context context) {
        this.context = context;
    }

    /**
     * 打印
     * 网页使用的js，方法有参数，且参数名为data
     * @param data 网页js里的参数名
     */
    @JavascriptInterface
    public void printClick(String data) {
        System.out.println("------打印----------");
        AidlUtil.getInstance().sendPrint(data);
    }

    /**
     * 获取设备的SN号
     */
    @JavascriptInterface
    public static String getSN() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else
            return Build.SERIAL;
    }

    /**
     *隐藏软键盘
     */
    @JavascriptInterface
    public  void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }
    /**
     *查询全部
     * @param id
     */
    @JavascriptInterface
    public String queryDataAllClick(String id) {

        return "";

    }




}
