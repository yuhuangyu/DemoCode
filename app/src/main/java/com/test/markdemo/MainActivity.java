package com.test.markdemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.markdemo.exercise.webviewtest.FloatWindow;
import com.test.markdemo.exercise.webviewtest.WebManager;
import com.test.markdemo.exercise.webviewtest.WebManager2;
import com.test.markdemo.use.UtilsUse;
import com.test.markdemo.utils.ActivityHooker;
import com.test.markdemo.utils.Hooker;
import com.test.markdemo.utils.InputSimulator;

import java.util.Random;

public class MainActivity extends Activity {
    private static Handler _handler = new Handler(Looper.getMainLooper());
    private Context _context;
    private WebView _webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //透明activity，点击事件可透过activity，必须在 super.onCreate 之前
//        UtilsUse.setActivityVisible(MainActivity.this,this);

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // View与Activity对象的自动点击， 自动滑动
//        UtilsUse.clickAndRoll(this, MainActivity.this);
        //hook Activity 的启动流程，与 StartActivity
//        UtilsUse.hookActivityAndstartActivity(this);
        // WebView注入js举例
//        UtilsUse.JSUsed(this);

        // FloatWindow  -- WindowManager 添加view显示窗口工具
        UtilsUse.showWindow(this);

        //替换本应用包名
//        UtilsUse.changePakageName(MainActivity.this);

//        WebView webView = new WebView(this);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return false;
//            }
//        });
//        webView.loadUrl("https://www.baidu.com/");
//        setContentView(webView);
        new WebManager2(this,null).setup("http://www.baidu.com/");

        //webview可在谷歌上调试
//        _handler.post(new Runnable() {
//            @Override
//            public void run() {
//                WebView.setWebContentsDebuggingEnabled(true); //todo
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        finish();
    }
    /*@Override
    protected void onResume() {

        finish();
       *//* new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                finish();
            }
        },10000);*//*
    }*/
}
