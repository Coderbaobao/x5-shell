package com.example.chuangjue;

import android.app.Application;
import android.util.Log;

import com.example.chuangjue.utils.AidlUtil;
import com.example.chuangjue.utils.LogUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

public class BaseApplication extends Application {
    private String TAG = "BaseApplication";



    @Override
    public void onCreate() {
        super.onCreate();


        initX5();
    }


    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                LogUtil.i("View是否初始化完成:" + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                LogUtil.i("X5内核初始化完成");
            }
        };

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                LogUtil.i("腾讯X5内核 下载结束");
            }

            @Override
            public void onInstallFinish(int i) {
                LogUtil.i("腾讯X5内核 安装完成");
            }

            @Override
            public void onDownloadProgress(int i) {
                LogUtil.i("腾讯X5内核 下载进度:%" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
