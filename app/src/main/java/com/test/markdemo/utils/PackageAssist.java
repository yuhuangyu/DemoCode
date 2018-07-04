package com.test.markdemo.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;



/**
 * Created by longyu on 2017/5/2.
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　＞　　　＜　┃
 * ┃　　　　　　　┃
 * ┃...　⌒　...　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * ┃　　　┃
 * ┃　　　┃
 * ┃　　　┃
 * ┃　　　┃  神兽保佑
 * ┃　　　┃  代码无bug
 * ┃　　　┃
 * ┃　　　┗━━━┓
 * ┃　　　　　　　┣┓
 * ┃　　　　　　　┏┛
 * ┗┓┓┏━┳┓┏┛
 * ┃┫┫　┃┫┫
 * ┗┻┛　┗┻┛
 */
public class PackageAssist {
    /**
     * 原来的包名
     */
    private String realPkg;
    /**
     * 替换后的包名
     */
    private String replacePkg;

    private static PackageAssist instance;

    private Object mApplication;

    public static PackageAssist getInstance() {
        if (instance == null) {
            synchronized (PackageAssist.class) {
                instance = new PackageAssist();
            }
        }
        return instance;
    }

    public static boolean isReplacePkg(String pkg) {
        if (getInstance().replacePkg == null || pkg == null)
            return false;
        return pkg.equals(getInstance().replacePkg);
    }

    public void init(Context mApplication) {
        realPkg = mApplication.getPackageName();
        this.mApplication = mApplication;
    }

    /**
     * 替换某一参数的intent包名
     *
     * @param args
     * @param index
     */
    public static void replaceIntentReal(Object[] args, int index) {
        if (args[index] != null && args[index] instanceof Intent) {
            Intent intent = (Intent) args[index];
            String arg = null;
            if (intent.getComponent() != null) {
                arg = intent.getComponent().getPackageName();
                if (arg != null && isReplacePkg(arg)) {
//                    ReflacUtlis.set(intent.getComponent(), "mPackage", getRealPkg());
                    ReflectAccess.setValue(intent.getComponent(), "mPackage", getRealPkg());
                }
            }

            if (!TextUtils.isEmpty(intent.getPackage()) && isReplacePkg(intent.getPackage())) {
                intent.setPackage(getRealPkg());
            }
        }
    }

    /**
     * 设置虚拟的包名替换真实包名
     *
     * @param replacePkg
     */
    public static void setReplacePkg(String replacePkg) {
        if (!TextUtils.isEmpty(getInstance().replacePkg))
            return;

        if (isReplacePkg(replacePkg))
            return;

        getInstance().replacePkg = replacePkg;

        if (getInstance().mApplication == null) {
            Log.e("123", "替换包名失败，获取application为null");
            return;
        }
        Object mBase = ReflectAccess.getValue(getInstance().mApplication, "mBase");
//        Object mBase = ReflacUtlis.get(getInstance().mApplication, "mBase");
        if (mBase instanceof VPackageContext) {
            VPackageContext packageContext = (VPackageContext) mBase;
            packageContext.setProviderPName(replacePkg);
            return;
        }
        VPackageContext packageContext = new VPackageContext((Context) mBase);
        packageContext.setProviderPName(replacePkg);
//        ReflacUtlis.set(getInstance().mApplication, "mBase", packageContext);
        ReflectAccess.setValue(getInstance().mApplication, "mBase", packageContext);
    }

    public static void replaceReal(Object[] args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    String arg = String.valueOf(args[i]);
                    if (PackageAssist.isReplacePkg(arg)) {
                        args[i] = PackageAssist.getRealPkg();
                    }
                } else {
                    if (args[i] instanceof Intent)
                        replaceIntentReal(args, i);
                }
            }
        }
    }

    public static String getRealPkg() {
        return getInstance().realPkg;
    }

    public Object getApplication() {
        return mApplication;
    }

    public static class VPackageContext extends ContextWrapper {

        private String providerPName;

        public void setProviderPName(String providerPName) {
            this.providerPName = providerPName;
        }

        public VPackageContext(Context base) {
            super(base);
        }

        @Override
        public Context getApplicationContext() {
            return (Context) PackageAssist.getInstance().getApplication();
        }

        @Override
        public String getPackageName() {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                // 广告的包名替换
//                if (element.getClassName().contains("chromium")
//                        || element.getClassName().contains("webkit")
//                        || element.getClassName().contains("com.qq")
//                        || element.getClassName().contains("com.baidu")) {

                    if (element.toString().contains("org.chromium.policy.AbstractAppRestrictionsProvider.refresh"))
                        return getRealPkg();

                    return providerPName;
//                }
            }
            return getRealPkg();
        }
    }
}
