package com.api.utils.myfb;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;


public class MainActivity extends Activity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);



//        System.setProperty("aid","dev_fujian");//appid由易加这边统一分配
//        System.setProperty("cid","dev_fujian");//channelid开发者可以自行定义
//        LiveApplication.fastCoupleIn(this);

        /*sdk = SDKFactory.getSDK();
//        sdk.init(this);
        sdk.init(this, new SDKInitListener() {
            @Override
            public void initSuccess() {
                Logger.I("initSuccess ");
                sdk.load(MainActivity.this,SDKFactory.INSERT);
            }

            @Override
            public void initFail(String var1) {
                Logger.I("initFail "+var1);
            }
        });*/

       /* try {
            Class<?> aClass = Class.forName("sdk.a.c.c.g");
            Field a1 = aClass.getField("a");
            Boolean ssss1 = (Boolean) a1.get(aClass);
            Log.e("sdk","11111111 "+ssss1);
            Method a = aClass.getMethod("a");
            Object invoke = a.invoke(aClass, new Object[]{});
            Field a3 = aClass.getField("a");
            Boolean ssss2 = (Boolean) a3.get(aClass);
            Log.e("sdk","22222222 "+ssss2);
//            g.a();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SDK sdk = SDKFactory.getSDK();
        sdk.load(MainActivity.this,SDKFactory.INSERT);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("sdk","===finish");
                MainActivity.this.finish();
            }
        },10000);*/

//        Properties properties = new Properties();
//        properties.load(project.rootProject.file('local.properties').newDataInputStream());
//
//        String user = properties.getProperty("bintray.user"); //读取 local.properties 文件里面的 bintray.user
//        String key = properties.getProperty("bintray.apikey");  //读取 local.properties 文件里面的 bintray.apikey

        /*Properties _properties = new Properties();
        File file = new File("myfb/src/data");
        if (file.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                _properties.load(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String user = _properties.getProperty("bintray.user", "");
        String key = _properties.getProperty("bintray.apikey", "");
        Log.e("sdk","==== user  "+user+"key"+key);*/
//        wakeUpAndUnlock(this);


        boolean keyguardSecure = checkPasswordToUnLock(this);
        Log.e("sdk","==== keyguardSecure  "+keyguardSecure+" ==  "+android.os.Build.VERSION.SDK_INT);

        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz&referer=aaa";
        id = Uri.parse(adsenseBannerUrl).getQueryParameter("id");
        Log.e("sdk","id -"+ id);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.android.vending");
            this.getApplicationContext().startActivity(intent);
//            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setPackage("com.android.vending"));
        } catch (Exception anfe) {
            Log.e("sdk","Exception -"+anfe.getMessage());
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
//        Uri parse = Uri.parse(adsenseBannerUrl);
//        Log.e("sdk","==== parse  "+parse);
//        Intent intent2 = new Intent(Intent.ACTION_VIEW);
//        intent2.setData(Uri.parse(adsenseBannerUrl));
//        intent2.setPackage("com.android.vending");
//        this.startActivity(intent2);
//        this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(adsenseBannerUrl)).setPackage("com.android.vending").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("sdk","== 10000 ");
                    MainActivity.this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (Exception anfe) {
                    Log.e("sdk","Exception -"+anfe.getMessage());
                    MainActivity.this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        },10000);*/

//       startActivity(new Intent(this,BlackActivity.class));

//        finish();
//        backToHome(this);

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
                    backToHome(MainActivity.this);
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                    Log.e("sdk", "==ACTION_SCREEN_OFF");

                    String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz";
                    String id = Uri.parse(adsenseBannerUrl).getQueryParameter("id");
                    Log.e("sdk","id -"+ id);

                    MainActivity.this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(adsenseBannerUrl)).setPackage("com.android.vending").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                    Log.e("sdk", "==ACTION_USER_PRESENT");
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.e("sdk", "==ACTION_USER_PRESENT "+android.os.Build.VERSION.SDK_INT);


                    }
                }
            }
        }, filter);*/

        String substring = adsenseBannerUrl.substring(adsenseBannerUrl.indexOf("id=")+3);
        Log.e("sdk", "==substring "+substring);

        try {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + substring)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            context.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(adsenseBannerUrl)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception anfe) {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + substring)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


    }


    //回到桌面
    public static void backToHome(Context context) {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(home);
    }


    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getApplicationContext()
                .getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁

//        pm.goToSleep(SystemClock.uptimeMillis());
    }


    private boolean checkPasswordToUnLock(Context context){
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardSecure();
    }

}
