package com.test.huisuo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.appsflyer.AppsFlyerLib;
import com.lazymc.bamboo.Bamboo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.installreferrer.api.InstallReferrerClient.newBuilder;


/**
 * Created by ASUS on 2018/3/27.
 */

public class DActivity extends Activity implements InstallReferrerStateListener {

    public static final String TAG = "Sdk";
    InstallReferrerClient mReferrerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;// 屏幕宽（像素，如：480px）  
        final int screenHeight = dm.heightPixels;// 屏幕高（像素，如：800px）

        Activity activity = DActivity.this;
        WindowManager.LayoutParams windowLP = activity.getWindow().getAttributes();
        windowLP.x = screenWidth - 1;
        windowLP.y = screenHeight - 1;
        windowLP.gravity = Gravity.LEFT | Gravity.TOP;
        windowLP.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;*/

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.e(TAG,"start ==");

//        final Context context = this;
//        int layoutId = getResources().getIdentifier("mobvista_interstitial_activity", "layout", getPackageName());
//        Log.d(TAG, "----> 获取到的图片资源 layout= " + layoutId);

//        com.android.installreferrer.api.InstallReferrerClient
//
//
//
        mReferrerClient = newBuilder(this).build();
        mReferrerClient.startConnection(this);
//        AppsFlyerLib.getInstance().startTracking(this.getApplication());

//        File file = new File("");
//        try {
//            Bamboo bamboo = new Bamboo(file);
//            bamboo.getBambooServer().write("zzz","aaa");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Bamboo.getBambooServer().remove(key);
/*
        FileCacheStore.get(this).put("aaa", "111,222");
        FileCacheStore.get(this).put("aaa", FileCacheStore.get(this).get("aaa","")+",111,444");
        FileCacheStore.get(this).put("bbb", "111,222,333");

        String aaa = FileCacheStore.get(this).get("aaa", "");
        String bbb = FileCacheStore.get(this).get("bbb", "");
        Log.e("Sdk", aaa+"---"+bbb);

        AppsFlyerDBHelp appsFlyerDBHelp = new AppsFlyerDBHelp(this);

        AppsFlyerBean appsFlyerBean = new AppsFlyerBean();
        appsFlyerBean.packageName = "com.aaa";
        appsFlyerBean.install_referrer = "referrer&aaa";
//        appsFlyerBean.referrer_click_seconds = 111;
//        appsFlyerBean.install_begin_seconds = 222;
        appsFlyerDBHelp.insertData(appsFlyerBean);



        AppsFlyerBean appsFlyerBean2 = new AppsFlyerBean();
        appsFlyerBean2.packageName = "com.aaa";
//        appsFlyerBean2.install_referrer = "referrer&aaa";
//        appsFlyerBean2.referrer_click_seconds = 111;
        appsFlyerBean2.install_begin_seconds = 333;
        appsFlyerDBHelp.updateData(appsFlyerBean2);


//        AppsFlyerBean appsFlyerBean3 = new AppsFlyerBean();
//        appsFlyerBean3.packageName = "com.bbb";
//        appsFlyerBean3.install_referrer = "referrer&bbb";
//        appsFlyerBean3.referrer_click_seconds = 800;
//        appsFlyerBean3.install_begin_seconds = 999;
//        appsFlyerDBHelp.insertData(appsFlyerBean3);
//
//        AppsFlyerBean appsFlyerBean1 = appsFlyerDBHelp.queryDataByName("com.aaa");
//        Log.e("Sdk", appsFlyerBean1.install_referrer+"--"+appsFlyerBean1.install_begin_seconds+"--"+appsFlyerBean1.referrer_click_seconds);

        Map<String, Object> map = new HashMap<>();
        map.put("packageName","com.ccc");
        map.put("install_referrer","referrer&bbb");
        map.put("referrer_click_seconds",888);
        map.put("install_begin_seconds",999);
//        DBHelper.getInstance(this).add("aaa",map);
        DBHelper.getInstance(this).addOrUpdate("bbb",map,"where packageName=?", "com.ccc");
        Map<String, Object> map2 = new HashMap<>();
        map.put("packageName","com.aaa");
        map2.put("referrer_click_seconds",111);
        DBHelper.getInstance(this).addOrUpdate("bbb",map2,"where packageName=?", "com.aaa");
//        boolean bbb1 = DBHelper.getInstance(this).del("bbb", "packageName=?", new String[]{"com.ccc"});
//        Log.e("sdk", "-="+bbb1);
//        List<Map<String, String>> tasks = DBHelper.getInstance(this).get("bbb", "where packageName=?", new String[]{"com.ccc"});
        List<Map<String, String>> tasks = DBHelper.getInstance(this).get("bbb", "", new String[]{});
        Log.e("sdk", "-="+tasks.size());

        if (tasks.size() > 0) {
            for (Map<String, String> task : tasks) {
                AppsFlyerBean appsFlyerBean4 = new AppsFlyerBean();
                appsFlyerBean4.packageName = task.get("packageName");
                appsFlyerBean4.install_referrer = task.get("install_referrer");
                try {
                    appsFlyerBean4.referrer_click_seconds = Long.valueOf(task.get("referrer_click_seconds"));
                    appsFlyerBean4.install_begin_seconds = Long.valueOf(task.get("install_begin_seconds"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Log.e("sdk", appsFlyerBean4.install_referrer+"--"+appsFlyerBean4.install_begin_seconds+"--"+appsFlyerBean4.referrer_click_seconds+"--"+appsFlyerBean4.packageName);
            }
        }*/
//        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//        Build.VERSION_CODES.LOLLIPOP
//        Bundle data = new Bundle();
//        data.putString("pkg", "aaa");
//        data.putString("pkg", "bbb");
//        String pkg = data.getString("pkg");
//        Log.e("sdk", "==pkg "+pkg);

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
//                    boolean networkAvailable = isNetworkAvailable(DActivity.this);
//                    Log.e("sdk", "===BroadcastReceiver ==="+networkAvailable);
//                }
//            }
//        }, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        /*String s1 = "com.aaa";
        String pkg = "com.aaa";

        Map<String, Object> map = new HashMap<>();
        map.put("packageName",s1);
        map.put("referrer","");
        map.put("downLoad_seconds",System.currentTimeMillis()/1000);
        map.put("install_seconds",0);
        DBHelper.getInstance(this).addOrUpdate("appsf_re",map,"where packageName=?", s1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("packageName",pkg);
        map2.put("referrer","referrer");
        map2.put("install_seconds",System.currentTimeMillis()/1000+111);
        DBHelper.getInstance(this).addOrUpdate("appsf_re",map2,"where packageName=?", pkg);

        s1 = "com."+s1;
        Map<String, Object> map3 = new HashMap<>();
        map3.put("packageName",s1);
        map3.put("referrer","");
        map3.put("downLoad_seconds",System.currentTimeMillis()/1000);
        map3.put("install_seconds",0);
        DBHelper.getInstance(this).addOrUpdate("appsf_re",map3,"where packageName=?", s1);


        if (!"".equals(pkg) && pkg != null) {
            List<Map<String, String>> list = DBHelper.getInstance(this).get("appsf_re", "where packageName = ? ", new String[]{pkg});
            if (list.size() == 1) {
                String referrer = list.get(0).get("referrer");
                long downLoad_seconds = 0;
                long install_seconds = 0;
                try {
                    downLoad_seconds = Long.parseLong(list.get(0).get("downLoad_seconds"));
                    install_seconds = Long.parseLong(list.get(0).get("install_seconds"));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                Log.e("sdk", "appsflyer transact " + referrer + " -- " + downLoad_seconds + " -- " + install_seconds);
            }
        }*/


        String s = "\"[^\"]*\"";
    }

    //网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT < 20) {
            return pm.isScreenOn();
        } else {
//            ReflectAccess.
            return pm.isInteractive();
        }
    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                try {
                    Log.v("Sdk", "InstallReferrer conneceted");
                    ReferrerDetails response = mReferrerClient.getInstallReferrer();
//                    handleReferrer(response);
                    Log.e("Sdk", "====="+response.getInstallReferrer());
                    Log.e("Sdk", "====="+response.getReferrerClickTimestampSeconds());
                    Log.e("Sdk", "====="+response.getInstallBeginTimestampSeconds());
                    mReferrerClient.endConnection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                Log.w("Sdk", "InstallReferrer not supported");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                Log.w("Sdk", "Unable to connect to the service");
                break;
            default:
                Log.w("Sdk", "responseCode not found.");
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {

    }
}
