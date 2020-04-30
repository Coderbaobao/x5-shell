package com.example.chuangjue;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.chuangjue.impl.MyJavascriptImpl;
import com.example.chuangjue.utils.AidlUtil;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


public class X5WebViewActivity extends AppCompatActivity  {

    private WebView webView;
    // 网页链接
    // private String mUrl;
   private String mUrl = "http://192.168.0.106";
 //   private String mUrl = "http://192.168.1.145:9092"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_x5);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getIntentData();
        initWebView();
        webView.loadUrl(mUrl);

    }

    private void getIntentData() {
        webView = findViewById(R.id.webview_detail);
        mUrl = getIntent().getStringExtra("mUrl");
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        ws.setSupportMultipleWindows(false);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。MIXED_CONTENT_ALWAYS_ALLOW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);
        //开启打印服务
        AidlUtil.getInstance().connectPrinterService(this);

        // 与js交互
        webView.addJavascriptInterface(new MyJavascriptImpl(this), "lee");
        webView.setWebChromeClient(new MyWebChromeClient());

     //   webView.loadUrl(mUrl);
//        webView.loadData("", "text/html", null);
//        webView.loadUrl("file:///android_asset/test.html");

    }

//    /**
//     * 打开网页:
//     *
//     * @param mContext 上下文
//     * @param mUrl     要加载的网页url
//     */
//    public static void loadUrl(Context mContext, String mUrl) {
//        Intent intent = new Intent(mContext, X5WebViewActivity.class);
//        intent.putExtra("mUrl", mUrl);
//        mContext.startActivity(intent);
//    }


    //WebChromeClient 主要辅助 WebView 处理J avaScript 的对话框、网站 Logo、网站 title、load 进度等处理
    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());

            builder.setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    Log.v("onJsAlert", "keyCode==" +  keyCode  + "event=" + event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            jsResult.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView webView, String url, String message, JsResult jsResult) {
            final JsResult result = jsResult;
            final AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
            builder.setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                    Log.v("onJsConfirm", "keyCode=="  + keyCode  + "event=" +  event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //返回网页上一页
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        Log.d("vi", "---onDestroy()--");
        try {
            if (webView != null) {
                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                webView.stopLoading();
                webView.setWebChromeClient(null);
                webView.setWebViewClient(null);
                webView.destroy();
                webView = null;
            }
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("vi", "---onPause()--");
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("vi", "---onResume()--");
        webView.onResume();
        super.onResume();
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        Log.d("vi", "---onStop()-- ");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        Log.d("vi", "---onRestart()--" );
        super.onRestart();
    }

}




