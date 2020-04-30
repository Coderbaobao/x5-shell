package com.example.chuangjue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private SharedPreferences pref;
    //用于判断是否是第一次运行，运行后变为false
    private boolean isFirst = true;
    private AutoCompleteTextView etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();
        isFirst();
    }

    private void initView() {
        findViewById(R.id.bt_openUrl).setOnClickListener(this);
        etSearch = findViewById(R.id.et_search);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_openUrl:
                openUrl();
                break;
            default:
                break;
        }
    }

    /**
     * 打开网页
     */
    private void openUrl() {
        String url = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            // 空url
            url = "https://www.baidu.com/";

        } else if (!url.startsWith("http") && url.contains("http")) {
            // 有http且不在头部
            url = url.substring(url.indexOf("http"), url.length());

        } else if (url.startsWith("www")) {
            // 以"www"开头
            url = "https://" + url;

        } else if (!url.startsWith("http") && (url.contains(".me") || url.contains(".com") || url.contains(".cn"))) {
            // 不以"http"开头且有后缀
            url = "https://www." + url;

        } else if (!url.startsWith("http") && !url.contains("www")) {
            // 输入纯文字 或 汉字的情况
            url = "http://m5.baidu.com/s?from=124n&word=" + url;
        }
        loadUrl(url);
    }
    private void loadUrl(String mUrl) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mURL", mUrl);//保存isFirstIn值为false
        editor.putBoolean("isFirstIn", false);//保存isFirstIn值为false
        editor.commit();//提交数据
        intentloadUrl(mUrl);
//        X5WebViewActivity.loadUrl(this, mUrl);

    }
    public  void intentloadUrl(String mUrl) {
        Intent intent = new Intent(this, X5WebViewActivity.class);
        intent.putExtra("mUrl", mUrl);
        startActivity(intent);
        finish();

    }


    private void isFirst() {
        pref = getSharedPreferences("shareData", MODE_PRIVATE);//创建SharedPreferences对象
        isFirst = pref.getBoolean("isFirstIn", true);//如果第一次运行，无isFirstIn值，自动获取第二个参数为默认值
        if (!isFirst) {//如果为true，进入if语句
            Log.e("MainActivity", "第二次运行  isFirst=false");
            String url = pref.getString("mURL","https://baidu.com/");
//            X5WebViewActivity.loadUrl(this, url);
            intentloadUrl(url);
        } else {
            Log.e("MainActivity","第一次运行  isFirst=true");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
