package com.api.dtest;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.api.hock.AdvertisingIdClient;

import net.vidageek.mirror.dsl.Mirror;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;
import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

public class MainActivity extends Activity implements SensorEventListener{


    public static String advertisingId;
    private final float[][] aaa = new float[2][];
    private final long[] bbb = new long[2];
    private double ddd;
    private long timeaaa;
    private WindowManager manager;
    private TextView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        String androidId_key = "";
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
//            androidId_key= Settings.Secure.ANDROID_ID;
//        }else{
//            androidId_key= Settings.System.ANDROID_ID;
//        }
//        String androidId=Settings.System.getString(this.getContentResolver(), androidId_key);
//        Log.e("sdk","=========== "+androidId);


        /*String path = Environment.getExternalStorageDirectory().getPath() + "/test/test1.apk";

        File file = new File(path);
        Log.e("sdk", "-- "+path+"  -- "+file.exists());
        AppManager.addPackage(this, file, new AppManager.InstallListener() {
            @Override
            public void onSuccess() {
                Log.e("sdk", "-onSuccess- ");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
//                Log.e("sdk", "-onError- "+e.toString());
//                if (e != null) {
//                    Log.e("sdk", "-onError- "+e.getMessage());
//                }
                String error = null;
                if (e != null) {
                    error = e.toString();
                    if (error.length()>60) {
                        error = error.substring(0,60);
                    }
                }
                Log.e("sdk", "-onError- "+error);
                Log.e("sdk", "22-onError- "+e);
            }
        });*/

        //打开通知监听服务
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            String serviceName = this.getPackageName()+ "/" + NNotificationServiceListener.class.getName();
//            AppManager.openNotificationListenerService(this, serviceName);
//        }

        //监测状态栏变化
//        NotificationHooker notificationHooker = new NotificationHooker();
//        notificationHooker.hookNotification(this);

//        try {
//            NotificationHooker.hookNotificationManager(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        /*String pkg = "com.android.providers.downloads";
//        String pkg = "com.android.vending";
//        String pkg = getPackageName();

        Intent intent = new Intent();

        //下面这种方案是直接跳转到当前应用的设置界面。
        //https://blog.csdn.net/ysy950803/article/details/71910806
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", pkg, null);
        intent.setData(uri);
        startActivity(intent);*/


//        ApplicationInfo appInfo = this.getApplicationInfo();
//        String pkg = this.getApplicationContext().getPackageName();
//        int uid = appInfo.uid;

        /*String pkg = "com.android.providers.downloads";
        int uid = 10006;

        boolean notificationEnabled = isNotificationEnabled(this,pkg,uid);
        Log.e("sdk", "notificationEnabled==== "+notificationEnabled);

//        String pkg = this.getApplicationContext().getPackageName();
        changeMode(pkg,uid);

        boolean notificationEnabled2 = isNotificationEnabled(this,pkg,uid);
        Log.e("sdk", "notificationEnabled2==== "+notificationEnabled2);


        getuid(pkg);*/

//        hockAdid(this);

//        int totalMemory = getTotalMemory();
//        Log.e("sdk","==   "+totalMemory);


//        long systeTotalMemorySize = getSysteTotalMemorySize();
//        long systemAvaialbeMemorySize = getSystemAvaialbeMemorySize();
//        Log.e("sdk","systeTotalMemorySize==   "+systeTotalMemorySize+"---systemAvaialbeMemorySize  "+systemAvaialbeMemorySize);

//        String romTotalSize = getRomTotalSize();
//        String romAvailableSize = getRomAvailableSize();
//        Log.e("sdk","romTotalSize==   "+romTotalSize+"---romAvailableSize  "+romAvailableSize);

//        tv_speed = (TextView) findViewById(R.id.tv_speed);

//        new NetWorkSpeedUtils(this).startShowNetSpeed();

//        String iccid = getIccid(this);
//        Log.e("sdk","  iccid==   "+iccid);


        /*String id = "aaaaa";
        Log.e("sdk","id2222 -"+ id);

        try {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setPackage("com.android.vending"));
        } catch (Exception anfe) {
            Log.e("sdk","Exception -"+anfe.getMessage());
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=" + id)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }*/


//        Log.e("sdk","2222macAddr   "+getMac());
//        String macAddress = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
//        Log.e("sdk","3333macAddr   "+macAddress);

        /*int versionCode = getGoogleVersionCode(this);
        Log.e("sdk", "versionCode =====  "+versionCode);


        String lngAndLat = null;
        try {
            lngAndLat = getLngAndLat(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sdk", "lngAndLat =====  "+lngAndLat);
        getLocation(this);
        try {
            TelephonyManager();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//        getAdid(this);
//        new CloudUtils(this,"","").invokeInstall();

//        TimeZone tz = TimeZone.getDefault();
//        String s = "TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" : " +tz.getID();
//        Log.e("sdk", s);
//        Log.e("sdk", "TimeZone =====  "+tz.getDisplayName(false, TimeZone.SHORT));
//
//        String codeName = android.os.Build.VERSION.CODENAME;
//
//        Log.e("sdk", "codeName  "+codeName);
//        getLocation2(this);

        /*Map<String, String> location = LocationUtils.getInstance(this).getLocation();
        Log.e("sdk", "location  "+location);
//
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, String> location = LocationUtils.getInstance(MainActivity.this).getLocation();
                Log.e("sdk", "location2222  "+location);
            }
        },25000);*/
//        for (int i = 0; i < 10; i++) {
//            LocationUtil.initLocation(this);
//            Map<String, String> location = LocationUtil.getLocation();
//            Log.e("sdk", "location2222  "+location);
//        }

       /* String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        Log.e("sdk", "language  "+language+" ,country  "+country);
        String locale = Locale.getDefault().toString();
        Log.e("sdk", "locale  "+locale);  // ja_JP  zh_CN
        TimeZone tz = TimeZone.getDefault();
        String s = "TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" : " +tz.getID();
        Log.e("sdk", s);

        int curBattery = getCurBattery(this);
        Log.e("sdk", "curBattery  "+curBattery);
        Log.e("sdk", "isLinkUSB  "+isLinkUSB(this));


        boolean isCharging = isCharging(this);

//        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        Log.e("sdk" ,"isCharging "+isCharging);
//        Log.e("sdk" ,"isCharging "+isCharging+" chargePlug "+chargePlug);
//        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
//        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        long time=System.currentTimeMillis();
        Log.e("sdk", "time "+time);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Log.e("sdk", "time "+str);

        SensorManager sensor = (SensorManager) this.getApplicationContext().getSystemService("sensor");
        Sensor defaultSensor = sensor.getDefaultSensor(1);
        Sensor defaultSensor2 = sensor.getDefaultSensor(2);
        Sensor defaultSensor4 = sensor.getDefaultSensor(4);

        Log.e("sdk", "defaultSensor "+defaultSensor+" ,defaultSensor2 "+defaultSensor2+" ,defaultSensor4 "+defaultSensor4);

        List<Sensor> sensorList1 = sensor.getSensorList(1);
        Log.e("sdk", "sensorList1 "+sensorList1);
        List<Sensor> sensorList2 = sensor.getSensorList(2);
        Log.e("sdk", "sensorList2 "+sensorList2);
        List<Sensor> sensorList4 = sensor.getSensorList(4);
        Log.e("sdk", "sensorList4 "+sensorList4);
//        for (int i = 0; i < sensorList1.size(); i++) {
//            boolean z;
//            int type = sensorList1.get(i).getType();
//            if (type < 0) {
//                z = false;
//            } else {
//                z = true;
//            }
//            Log.e("sdk", "sensorList=== "+sensorList1.get(i)+" -- "+z);
//
//        }
//        AppLovinSdk.initializeSdk(this);
        String userAgent = getUserAgent(this);
        Log.e("sdk", "userAgent "+userAgent);

        sensor.registerListener(this, sensor.getDefaultSensor(1), 0);
        sensor.registerListener(this, sensor.getDefaultSensor(2), 0);
        sensor.registerListener(this, sensor.getDefaultSensor(4), 0);


        Log.e("sdk", "bbb "+bbb);*/


//
//        String str = "";
//        String pattern = "^=@000$";
//
//        Pattern r = Pattern.compile(pattern, Pattern.MULTILINE);
//        Matcher m = r.matcher(str);
//        System.out.println(m.matches());

//        String app = "hcom.apk";
//        String path = Environment.getExternalStorageDirectory().getPath() + "/test/"+app;
//        Log.e("sdk", "path "+new File(path).exists());
//        String getpkg = getpkg(path);
//
//        Log.e("sdk", "getpkg "+getpkg);

        /*Map<String, Object> map = new HashMap<>();
        map.put("packageName","com.ccc");
        map.put("install_referrer","referrer&bbb");
        map.put("referrer_click_seconds",888);
        map.put("install_begin_seconds",999);
//        DBHelper.getInstance(this).add("aaa",map);
        DBHelper.getInstance(this).addOrUpdate("bbb",map,"where packageName=?", "com.ccc");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("packageName","com.aaa");
        map2.put("referrer_click_seconds",111);
        map2.put("install_referrer","referrer&bbb");
        map2.put("install_begin_seconds",999);
        DBHelper.getInstance(this).addOrUpdate("bbb",map2,"where packageName=?", "com.aaa");
//        boolean bbb1 = DBHelper.getInstance(this).del("bbb", "packageName=?", new String[]{"com.ccc"});
//        Log.e("sdk", "-="+bbb1);
//        List<Map<String, String>> tasks = DBHelper.getInstance(this).get("bbb", "where packageName=?", new String[]{"com.ccc"});
        List<Map<String, String>> tasks = DBHelper.getInstance(this).get("bbb", "", new String[]{});
        Log.e("sdk", "-="+tasks.size());

        if (tasks.size() > 0) {
            for (Map<String, String> task : tasks) {

                *//*AppsFlyerBean appsFlyerBean4 = new AppsFlyerBean();
                appsFlyerBean4.packageName = task.get("packageName");
                appsFlyerBean4.install_referrer = task.get("install_referrer");
                try {
                    appsFlyerBean4.referrer_click_seconds = Long.valueOf(task.get("referrer_click_seconds"));
                    appsFlyerBean4.install_begin_seconds = Long.valueOf(task.get("install_begin_seconds"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Log.e("sdk", appsFlyerBean4.install_referrer+"--"+appsFlyerBean4.install_begin_seconds+"--"+appsFlyerBean4.referrer_click_seconds+"--"+appsFlyerBean4.packageName);
            *//*}
        }*/
//        LocationUtil.initLocation(this);
//
//        Map<String, String> location = LocationUtil.getLocation();
//        Log.e("sdk", "location1111  "+location);
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, String> location2 = LocationUtil.getLocation();
//                Log.e("sdk", "location2222  "+location2);
//            }
//        },25000);

/*
//        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.tcsdzz&referer=aaa";
        String adsenseBannerUrl = "https://play.google.com/store/apps/details?id=com.hwgg.pk";
        final String substring = adsenseBannerUrl.substring(adsenseBannerUrl.indexOf("id=")+3);
        Log.i("sdk","suburl -"+substring);
        try {
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + substring)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setPackage("com.android.vending"));
//            context.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(adsenseBannerUrl)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception anfe) {
            Log.i("sdk","Exception -"+anfe.getMessage());
            this.getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + substring)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }*/


//        String appLocation = getAppLocation(this);
//        Log.e("sdk","appLocation -"+appLocation);
//
//        if (this.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
//            Log.e("sdk","WRITE_SECURE_SETTINGS  ===== -");
//        }else {
//            Log.e("sdk","WRITE_SECURE_SETTINGS  ===== - else ");
//        }


//        String s1 = randNumStar("0,0,0,10,10");
//        Log.e("sdk","randNumStar "+s1);
//        startAmazonByBrowser(this);

        startAmazonWeb();

//        inputText("很好玩啊000");
//        long commentTime = getCommentTime(this);
//        Log.e("sdk", "commentTime "+commentTime);
//        if (commentTime == -1) {
//            Log.e("sdk", "commentTime return ");
//            return;
//        }

//        SYSTEM_ALERT_WINDOW
        /*Log.e("sdk", "SYSTEM_ALERT_WINDOW  permission  =====  ");
        if (Build.VERSION.SDK_INT >= 25 && this.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            Log.e("sdk", "SYSTEM_ALERT_WINDOW not permission");
//            return;
        }
        boolean appOps = getAppOps(this);
        boolean common = commonROMPermissionCheck(this);
        Log.e("sdk", "SYSTEM_ALERT_WINDOW  getAppOps  =====  "+appOps);
        Log.e("sdk", "SYSTEM_ALERT_WINDOW  commonROMPermissionCheck  =====  "+common);


        manager = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        view = new TextView(this);
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
        WindowManager.LayoutParams windowManagerParams = getWindowManagerParams(this,max);
        try {
            manager.addView(view, windowManagerParams);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sdk", "SYSTEM_ALERT_WINDOW    e =====  "+e);
//            return;
        }

        Log.e("sdk", "manager addView "+max);
        int delay = (10) * 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager != null) {
                    Log.e("sdk", "manager removeViewImmediate ");
                    manager.removeViewImmediate(view);
                }
//                Utils.backToHome(context);
            }
        },delay);*/
    }

    private void startAmazonWeb() {
        final WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("sdk","onReceivedError "+error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("sdk","onPageStarted "+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("sdk","onPageFinished "+url);
                Log.e("sdk","onPageFinished2 "+url.replace("https","com.amazon.mobile.shopping.web"));
//                try {
//                    if (manager != null) {
//                        manager.removeViewImmediate(webView);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                startAmazonByBrowser(this);

                try {
                    if (manager != null) {
                        manager.removeViewImmediate(webView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                webView.destroy();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("https","com.amazon.mobile.shopping.web")));//yes
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));//yes
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                browserIntent.setPackage("com.amazon.mShop.android.shopping");
                MainActivity.this.startActivity(browserIntent);
            }
        });

        webView.loadUrl("https://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200");
//        webView.loadUrl("com.amazon.mShop.android.shopping://www.amazon.cn/?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200");
        manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //TYPE_SYSTEM_ERROR能够全屏显示
        params.format = 1;
        params.flags =  FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCH_MODAL | FLAG_NOT_TOUCHABLE;
        params.x = 0;//窗口位置的偏移量
        params.y = 0;

        params.alpha = 1;//窗口的透明度
        manager.addView(webView, params);
    }

    //判断权限
    private boolean commonROMPermissionCheck(Context context) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                Log.e("sdk", Log.getStackTraceString(e));
            }
        }
        return result;
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许 false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    public static WindowManager.LayoutParams getWindowManagerParams(Context context, int max) {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = max;
        lp.height = max;


        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        if (Build.VERSION.SDK_INT >= 25) {
//            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//        }
        lp.format = PixelFormat.TRANSLUCENT;

        lp.flags = 17368856;
        lp.dimAmount = -1f;
        lp.gravity = 8388659;
        lp.buttonBrightness = BRIGHTNESS_OVERRIDE_OFF;
        lp.systemUiVisibility = SYSTEM_UI_FLAG_LOW_PROFILE;

//        lp.alpha = 0.5f;//窗口的透明度
        return lp;
    }

    private long getCommentTime(Context context) {
        long commentTime = -1;
        String comment = "";
        if (comment != null && !"".equals(comment)) {
            String[] split = comment.split(",");
            if (split.length == 2) {
                int commentRate = 0;

                try {
                    commentRate = Integer.parseInt(split[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Random _rand = new Random();
                int value = _rand.nextInt(100); // 0 - 99
                Log.e("sdk","aso commentRate  "+commentRate+" -value "+value);
                if (value < commentRate) {
                    commentTime = 0l;
                    try {
                        commentTime = Long.parseLong(split[1]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return commentTime;
    }

    public void inputText(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        // 获取剪贴板的剪贴数据集
        ClipData clipData = clipboard.getPrimaryClip();
//        CharSequence text2 = "     ";
//        if (clipData != null && clipData.getItemCount() > 0) {
//            text2 = clipData.getItemAt(0).getText();
//            Log.e("sdk","text "+text2);
//        }

        ClipData clip = ClipData.newPlainText("null", text);
        clipboard.setPrimaryClip(clip);

        clipboard.setPrimaryClip(ClipData.newPlainText("null", null));
//        clipboard.setPrimaryClip(ClipData.newPlainText("null", ""));
        return;
    }

    public static void startAmazonByBrowser(Context ctx) {
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.cyou.cma.clauncher"));
//        https://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200

        //主页面                --- com.amazon.mShop.android.home.PublicUrlActivity ,targetActivity="com.amazon.mShop.splashscreen.StartupActivity"
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.amazon.mobile.shopping://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//yes
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.amazon.mobile.shopping.web://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//yes
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.amazon.mShop.android.shopping://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//yes

        //clouddrive 备份照片页面    --- com.amazon.mShop.clouddrive.CloudDriveUploadActivity
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mshop://clouddrive?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//yes

        // amazon http mshop      --- com.amazon.mShop.details.ProductDetailsActivity
        // 反编译：scheme:  amazonmobile(链接含asin参数) 、mshop(链接含ASIN参数)        --- com.amazon.mShop.details.ProductDetailsActivity
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("amazon://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no
//        Intent browserIntent = new Intent("com.amazon.shop.DETAIL_PAGE", Uri.parse("http://amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mshop://www.amazon.cn/dp/B01LYGHUTZ/ref=cngwdyfloorv2_recs_0?pf_rd_p=d0690322-dfc8-4e93-ac2c-8e2eeacbc49e&pf_rd_s=desktop-2&pf_rd_t=36701&pf_rd_i=desktop&pf_rd_m=A1AJ19PSB66TGU&pf_rd_r=H85JVJGKQ21BVRYVEP98&pf_rd_r=H85JVJGKQ21BVRYVEP98&pf_rd_p=d0690322-dfc8-4e93-ac2c-8e2eeacbc49e"));//no

        // scheme: http、https  host:www.amazon.co.uk、www.amazon.co.jp、www.amazon.de、www.amazon.com
        // --- com.amazon.mShop.aiv.AivHttpIntentActivity
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.com?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no

        //file     receiver  --- com.amazon.clouddrive.library.device.receivers.ExternalMediaChangeReceiverProxy
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("file://www.amazon.cn?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no

        //  com.amazon.mShop.httpUrlDeepLink.HttpUrlDeepLinkingActivity
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://amzn.asia?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.cn/dp/?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no


//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/openInMobileApp?&_encoding=UTF8&tag=mengbao-23&linkCode=ur2&linkId=b027e09eafd410e9190abfa8a4a9d65f&camp=536&creative=3200"));//no


        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        browserIntent.setPackage("com.amazon.mShop.android.shopping");
//        browserIntent.setPackage("com.android.chrome");
//        browserIntent.setClassName("cn.amazon.mShop.android","com.amazon.mShop.splashscreen.StartupActivity");
//        browserIntent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        ctx.startActivity(browserIntent);



    }

    private String randNumStar(String numStr) {
        String[] split = numStr.split(",");
        if (split != null && split.length == 5) {
            int star1 = Integer.parseInt(split[0]);
            int star2 = Integer.parseInt(split[1]);
            int star3 = Integer.parseInt(split[2]);
            int star4 = Integer.parseInt(split[3]);
            int star5 = Integer.parseInt(split[4]);
            int i = randInt(1, star1 + star2 + star3 + star4 + star5);
            Log.e("sdk","aso star "+numStr+" - "+i);
            if (1 <= i &&  i <= star1) {
                return "1";
            } else if (star1+1 <= i &&  i <= (star1+star2)) {
                return "2";
            } else if (star1+star2+1 <= i &&  i <= (star1+star2+star3)) {
                return "3";
            } else if (star1+star2+star3+1 <= i &&  i <= (star1+star2+star3+star4)) {
                return "4";
            } else if (star1+star2+star3+star4+1 <= i &&  i <= (star1+star2+star3+star4+star5)) {
                return "5";
            }
        }
        return "5";
    }

    public int randInt(int min, int max)
    {
        Random _rand = new Random();
        return _rand.nextInt(max - min + 1) + min;
    }

    public static String getAppLocation(Context context)
    {
        ApplicationInfo info = context.getApplicationInfo();

        boolean res = hasInstallPermission(context);

        int v = 0;
        if (res)
            v = 1;

        if (info.sourceDir.toLowerCase().contains("/data/app"))
        {
            return "data:" + v;
        }
        else if (info.sourceDir.toLowerCase().contains("/system/app"))
        {
            return "sys-app:" + v;
        }
        else if (info.sourceDir.toLowerCase().contains("/system/priv-app"))
        {
            return "sys-priv:" + v;
        }
        else
            return "none:" + v;
    }

    public static boolean hasInstallPermission(Context context)
    {
        return context.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == PackageManager.PERMISSION_GRANTED;
    }

    private String getpkg(String path) {
        PackageInfo packageArchiveInfo = this.getPackageManager().getPackageArchiveInfo(path, 0);
        if (packageArchiveInfo == null){
            return null;
        }
        String packageName = packageArchiveInfo.packageName;
        return packageName;
    }


    private static double ccc(float[] fArr, float[] fArr2) {
        double d = 0.0d;
        for (int i = 0; i < Math.min(fArr.length, fArr2.length); i++) {
            d += StrictMath.pow((double) (fArr[i] - fArr2[i]), 2.0d);
        }
        return Math.sqrt(d);
    }


    private static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 是否在充电
     *
     */
    private boolean isCharging(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Intent batteryStatus = context.registerReceiver(null, ifilter);
        //你可以读到充电状态,如果在充电，可以读到是usb还是交流电
        // 是否在充电
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }

    /**
     * 获取设备当前电量
     *
     * @return 90 电量90%
     */
    private int getCurBattery(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
            int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            Log.e("sdk", "battery = " + battery);
            return battery;
        } else {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryIntent = mContext.registerReceiver(null, intentFilter);
            if (null != batteryIntent) {
                int rst = batteryIntent.getIntExtra("level", 0);
                Log.e("sdk", "battery level = " + rst);
                return rst;
            }
            return 100;
        }
    }

    /**
     * 是否通过数据线连接usb调试
     *
     * @return true 设备使用数据线连接了usb调试，异常
     */
    boolean isLinkUSB(Context mContext) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = mContext.registerReceiver(null, ifilter);
        //如果设备正在充电，可以提取当前的充电状态和充电方式（无论是通过 USB 还是交流充电器），如下所示：

        // Are we charging / charged?
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (isCharging) {
            if (usbCharge) {
                return true;  //连接usb且在调试状态
            } else if (acCharge) {
                return false;  //连接usb在充电
            }
        } else {
            return false;   //未连接usb
        }
        return true;
    }

    private void getLocation2(Context context) {
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders( true );
        String locationProvider = null;
        Log.e("sdk", "== providers "+providers);
        if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains( LocationManager.GPS_PROVIDER )) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation( locationProvider );
        if (location != null) {
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
//            loc_lat = location.getLatitude();
//            loc_lng= location.getLongitude();
            Log.e("sdk", "== address "+address);
        }else {
            locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
            Location location2 = locationManager.getLastKnownLocation(locationProvider);
            if (location2 != null) {
                double latitude2 = location2.getLatitude();
                double longitude2 = location2.getLongitude();
                Log.e("sdk", "== address222 "+latitude2+" - "+longitude2);
            }
        }
    }


    protected void getAdid(final Context base) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.api.yunkong.AdvertisingIdClient.AdInfo adInfo = null;
                try {
                    adInfo = com.api.yunkong.AdvertisingIdClient.getAdvertisingIdInfo(base);
                    if (adInfo != null) {
                        advertisingId = adInfo.getId();
                        Log.e("sdk", "AdvertisingId====id "+ advertisingId);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }

    private String getMac() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        if (adapter == null) {
//            adapter = (BluetoothAdapter) getApplicationContext().getSystemService(BLUETOOTH_SERVICE);
//        }
        String macAddr = adapter.getAddress();
        Log.e("sdk","1111macAddr   "+macAddr);
        Object bluetoothManageService = new Mirror().on(adapter).get().field("mService");
        if (bluetoothManageService == null)
            return null;
        Object address = new Mirror().on(bluetoothManageService).invoke().method("getAddress").withoutArgs();
        if (address != null && address instanceof String) {
            return (String) address;
        } else {
            return null;
        }
    }

    private void getuid(String pkg) {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkg, PackageManager.GET_ACTIVITIES);
            Log.e("sdk", "=====uid " + ai.uid);
//            Toast.makeText(MainActivity.this, Integer.toString(ai.uid,10), Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void changeMode(String pkg, int uid) {
        AppOpsManager mAppOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
//        mAppOps.setMode(11, uid, pkg,
//                AppOpsManager.MODE_IGNORED);

//        ApplicationInfo appInfo = this.getApplicationInfo();
//
//        int uid = appInfo.uid;
        Class appOpsClass = null;
        try {
            if (this.checkPermission("android.permission.UPDATE_APP_OPS_STATS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
                Log.e("sdk","UPDATE_APP_OPS_STATS open failed");
                return ;
            }
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method setModeMethod = appOpsClass.getMethod("setMode", Integer.TYPE, Integer.TYPE,
                    String.class, Integer.TYPE);
            setModeMethod.invoke(mAppOps,11, uid, pkg, AppOpsManager.MODE_IGNORED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNotificationEnabled(Context context, String pkg, int uid) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        Log.e("sdk", "==== "+uid);
        Class appOpsClass = null;
   /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void hockAdid(final Context base) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AdvertisingIdClient.AdInfo adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(base);
                    if (adInfo != null) {
                        String id = adInfo.getId();
                        Log.e("sdk","2222====id "+id);

                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }

    private int getTotalMemory() {
        String str1 = "/proc/meminfo";
        int initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            String str2 = localBufferedReader.readLine();
            String[] arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
            localBufferedReader.close();
        } catch (IOException var7) {
            ;
        }

        return initial_memory;
    }


    /**
     * 获取系统内存大小
     * @return
     */
    private long getSysteTotalMemorySize(){
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo) ;
        long memSize = memoryInfo.totalMem ;
        //字符类型转换
//        String availMemStr = formateFileSize(memSize);
        return memSize ;
    }

    /**
     * 获取系统可用的内存大小
     * @return
     */
    private long getSystemAvaialbeMemorySize(){
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo) ;
        long memSize = memoryInfo.availMem ;

        //字符类型转换
//        String availMemStr = formateFileSize(memSize);

        return memSize ;
    }


    /**
     * 获得机身内存总大小
     *
     * @return
     */
    private String getRomTotalSize() {
        File path = Environment.getRootDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        Log.e("sdk", "getRomTotalSize  "+ blockSize * totalBlocks);
        return Formatter.formatFileSize(MainActivity.this, blockSize * totalBlocks);
    }
    /**
     * 获得机身可用内存
     *
     * @return
     */
    private String getRomAvailableSize() {
        File path = Environment.getRootDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        Log.e("sdk", "getRomAvailableSize  "+ blockSize * availableBlocks);
        return Formatter.formatFileSize(MainActivity.this, blockSize * availableBlocks);
    }


    //获取sim卡iccid
    public String getIccid(Context context) {
        String iccid = "N/A";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        iccid = telephonyManager.getSimSerialNumber();
        return iccid;
    }

    public int getGoogleVersionCode(Context context) {

        PackageManager pckMan = context.getPackageManager();
//        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);

        for (PackageInfo pInfo : packageInfo) {
//            HashMap<String, Object> item = new HashMap<String, Object>();
//            item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
//            item.put("packageName", pInfo.packageName);
//            item.put("versionCode", pInfo.versionCode);
//            item.put("versionName", pInfo.versionName);
//            item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());
//            items.add(item);
            if ("com.android.vending".equals(pInfo.packageName)) {
                Log.e("sdk", "com.android.vending  "+ pInfo.versionCode);
//                return pInfo.versionCode;
            } else if ("com.google.android.gms".equals(pInfo.packageName)) {
                Log.e("sdk", "com.google.android.gms  "+ pInfo.versionCode);
//                return pInfo.versionCode;
            } else if ("com.google.android.stf".equals(pInfo.packageName)) {
                Log.e("sdk", "com.google.android.stf  "+ pInfo.versionCode);
//                return pInfo.versionCode;
            } else if ("com.google.android.stf.login".equals(pInfo.packageName)) {
                Log.e("sdk", "com.google.android.stf.login  "+ pInfo.versionCode);
//                return pInfo.versionCode;
            } else if ("com.google.android.play.games".equals(pInfo.packageName)) {
                Log.e("sdk", "com.google.android.play.games  "+ pInfo.versionCode);
//                return pInfo.versionCode;
            }

        }
        return -1;
    }


    private String getLngAndLat(Context context) {
        double latitude = 0.0;
        double longitude = 0.0;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        //2.获取位置提供器，GPS或是NetWork
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //设置精确度
        criteria.setAltitudeRequired(false); //设置高度
        criteria.setBearingRequired(false); //设置轴承
        criteria.setCostAllowed(true); //容许成本
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation( locationManager.getBestProvider(criteria, true) );
        String address = null;
        if (location != null) {
            address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();

//            location
        }
        return address;
    }

    private void getLocation(Context mContext) {
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) mContext.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders( true );
        String locationProvider = null;
        if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            //如果是网络定位
            Log.d( "sdk", "如果是网络定位" );
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains( LocationManager.GPS_PROVIDER )) {
            //如果是GPS定位
            Log.d( "sdk", "如果是GPS定位" );
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Log.d( "sdk", "没有可用的位置提供器" );
            return;
        }
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        /*if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation( locationProvider );
        if (location != null) {
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            Log.d( "sdk", "getLocation == "+address );
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
//        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener );


    }

    public void TelephonyManager(){
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回值MCC + MNC
        String operator = mTelephonyManager.getNetworkOperator();
        Log.e("sdk", "operator  "+operator);
        int mcc = 0;
        int mnc = 0;
        try {
            mcc = Integer.parseInt(operator.substring(0, 3));
            mnc = Integer.parseInt(operator.substring(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 中国移动和中国联通获取LAC、CID的方式
        GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();
        int lac = 0;
        int cellId = 0;
        if (location != null) {
            lac = location.getLac();
            cellId = location.getCid();
        }

        Log.e("sdk", " MCC = " + mcc + "\t MNC = " + mnc + "\t LAC = " + lac + "\t CID = " + cellId);

        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation cel = tel.getCellLocation();
        int nPhoneType = tel.getPhoneType();
        //移动联通 GsmCellLocation
        if(cel instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
            int nGSMCID = gsmCellLocation.getCid();
            if (nGSMCID > 0) {
                if (nGSMCID != 65535) {
//                    this.cell = nGSMCID;
                    int lac2 = gsmCellLocation.getLac();
                    Log.e("sdk", "移动联通   cell "+ nGSMCID+" ,lac  "+lac2);
                }
            }
        }
        if (/*nPhoneType == 2 && */cel instanceof CdmaCellLocation) {
            Log.e("电信", "-----------------》电信");
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
            int sid = cdmaCellLocation.getSystemId();
            int lac3 = cdmaCellLocation.getNetworkId();
            int cid3 = cdmaCellLocation.getBaseStationId();
            Log.e("sdk", "电信   sid "+ sid+" ,nid  "+lac3+" ,bid  "+cid3);
        }
        Log.e("sdk", "nPhoneType   nPhoneType "+ nPhoneType+" ,cel  "+cel);

        /*if (type == TelephonyManager.NETWORK_TYPE_CDMA        // 电信cdma网
                || type == TelephonyManager.NETWORK_TYPE_1xRTT
                || type == TelephonyManager.NETWORK_TYPE_EVDO_0
                || type == TelephonyManager.NETWORK_TYPE_EVDO_A
                || type == TelephonyManager.NETWORK_TYPE_LTE )   {
            CdmaCellLocation cdma = (CdmaCellLocation) manager.getCellLocation();
            if (cdma == null)
            {
                return null;
            }
            int  lac = cdma.getNetworkId();
            int  cid = cdma.getBaseStationId();*/
    }

    //从网络获取经纬度
    public String getLngAndLatWithNetwork(Context context) {
        double latitude = 0.0;
        double longitude = 0.0;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        return longitude + "," + latitude;
    }

    LocationListener locationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("sdk", "Location changed : Lat: "
                        + location.getLatitude() + " Lng: "
                        + location.getLongitude());
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*if (sensorEvent.sensor.getType() == 1) {
                    double p = (double) sensorEvent.values[0];
                    double q = (double) sensorEvent.values[1];
                    double r = (double) sensorEvent.values[2];
                    Log.e("sdk", "p "+p+" ,q "+q+" ,r "+r);
//                    this.z = (TextView) findViewById(2131296529);
//                    textView = this.z;
//                    stringBuilder = new StringBuilder();
//                    stringBuilder.append("X : ");
//                    stringBuilder.append(String.format("%.2f", new Object[]{Double.valueOf(this.p)}));
//                    stringBuilder.append(" m/s² \nY : ");
//                    stringBuilder.append(String.format("%.2f", new Object[]{Double.valueOf(this.q)}));
//                    stringBuilder.append(" m/s² \nZ : ");
//                    str = "%.2f";
//                    objArr = new Object[]{Double.valueOf(this.r)};
                }*/
        if (sensorEvent != null && sensorEvent.values != null) {
            int i;
            Sensor sensor = sensorEvent.sensor;
            if (sensor == null || sensor.getName() == null || sensor.getVendor() == null) {
                i = 0;
            } else {
                i = 1;
            }
            if (i != 0) {
                i = sensorEvent.sensor.getType();
                String name = sensorEvent.sensor.getName();
                String vendor = sensorEvent.sensor.getVendor();
                long j = sensorEvent.timestamp;
                float[] fArr = sensorEvent.values;
                if (i == 1 || i == 2 || i == 4) {
                    long currentTimeMillis = System.currentTimeMillis();
                    float[] fArr2 = aaa[0];
                    if (fArr2 == null) {
                        aaa[0] = Arrays.copyOf(fArr, fArr.length);
                        bbb[0] = currentTimeMillis;
                        return;
                    }
                    float[] fArr3 = aaa[1];
                    if (fArr3 == null) {
                        fArr3 = Arrays.copyOf(fArr, fArr.length);
                        aaa[1] = fArr3;
                        bbb[1] = currentTimeMillis;
                        ddd = ccc(fArr2, fArr3);
                    } else if (50000000 <= j - timeaaa) {
                        timeaaa = j;
                        if (Arrays.equals(fArr3, fArr)) {
                            bbb[1] = currentTimeMillis;
                            return;
                        }
                        double ttt = ccc(fArr2, fArr);
                        if (ttt > ddd) {
                            aaa[1] = Arrays.copyOf(fArr, fArr.length);
                            bbb[1] = currentTimeMillis;
                            ddd = ttt;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
