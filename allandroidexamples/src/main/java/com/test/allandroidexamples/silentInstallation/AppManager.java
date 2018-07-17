package com.test.allandroidexamples.silentInstallation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class AppManager {
    public interface InstallListener {
        void onSuccess();

        void onError(Throwable e);
    }

    public static List<String> getInstalledApps(Context context) {
        List<ApplicationInfo> list = context.getPackageManager().getInstalledApplications(0);
        List<String> packages = new ArrayList<String>();
        for (ApplicationInfo info : list) {
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                packages.add(info.packageName);
        }

        return packages;
    }

    public static String getPackageNameByPath(Context context, String apkPath) {
        if (!apkPath.endsWith(".apk")) {
            return "";
        }
        PackageManager manager = context.getPackageManager();
        PackageInfo info = manager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        String result = "";
        if (info != null) {
            result = info.applicationInfo.packageName;
        }
        return result;
    }

    public static boolean isInstalled(Context context, String pkg) {
        try {
            return context.getPackageManager().getPackageInfo(pkg, 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void rmPackage(Context ctx, String packageName, InstallListener listener) {

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Pm21.runUninstall(ctx, packageName, listener);
        } else {
            Pm20.runUninstall(ctx, packageName, listener);
        }

    }

    /*public static void rmPackageAuto(Context context, String packageName) {
        String serviceName = context.getPackageName()+ "/" + Config.get(context).get("ap.autoAccessibility", AutoAccessibilityService.class.getName());
        Log.i("sdk","serviceName = " + serviceName);
        OtherManager.startAutoService(context, serviceName);
        try {
            Thread.sleep(AutoAccessibilityService.AUTO_TIME_SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!OtherManager.isAutoServiceOn(context, serviceName)) {
            Log.i("sdk","Accessibility is disabled and can not open, , please manual open");
            return;
        }
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }*/

    public static void addPackage(Context ctx, File archiveFile, InstallListener listener) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Pm21.runInstall(ctx, archiveFile, listener);
        } else {
            Pm20.runInstall(ctx, archiveFile, listener);
        }
    }

    public static void addPackageByUser(Context ctx, File archiveFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(archiveFile), "application/vnd.android.package-archive");

        ctx.startActivity(intent);
    }

    public static class Pm20 {
        public static void runUninstall(Context mCtx, String packageName, final InstallListener listener) {
            try {
                Method methodDeletePackage = PackageManager.class.getDeclaredMethod("deletePackage", String.class, IPackageDeleteObserver.class, int.class);
                IPackageDeleteObserver observer = new IPackageDeleteObserver.Stub() {
                    @Override
                    public void packageDeleted(String s, int i) throws RemoteException {
                        if (i == 1) {
                            if (listener != null)
                                listener.onSuccess();
                        } else {
                            if (listener != null)
                                listener.onError(new RuntimeException(s + " uninstall error"));
                        }
                    }
                };

                methodDeletePackage.invoke(mCtx.getPackageManager(), packageName, observer, 0);
            } catch (Throwable e) {
                if (listener != null)
                    listener.onError(e);
            }

        }


        public static void runInstall(Context mCtx, File archiveFile, final InstallListener listener) {

            int installFlags = 64; //  PackageManager.INSTALL_ALL_USERS
            installFlags |= 2;     // PackageManager.INSTALL_REPLACE_EXISTING;
            installFlags |= 128;   // PackageManager.INSTALL_ALLOW_DOWNGRADE;
            try {

                Uri uri = Uri.fromFile(archiveFile);
                Method methodInstallPackage = PackageManager.class.getDeclaredMethod("installPackage", Uri.class, IPackageInstallObserver.class, int.class, String.class);
                IPackageInstallObserver observer = new IPackageInstallObserver.Stub() {
                    @Override
                    public void packageInstalled(String s, int i) throws RemoteException {
                        if (s != null && i == 1) {
                            if (listener != null)
                                listener.onSuccess();
                        } else {
                            if (listener != null)
                                listener.onError(new RuntimeException("install error"+i));
                        }
                    }
                };
                methodInstallPackage.invoke(mCtx.getPackageManager(), uri, observer, installFlags, null);
            } catch (Throwable e) {
                if (listener != null)
                    listener.onError(e);
            }

        }

    }

    public static class Pm21 {
        public static void runUninstall(Context mCtx, String packageName, final InstallListener listener) {
            try {
                Method methodDeletePackage = PackageManager.class.getDeclaredMethod("deletePackage", String.class, IPackageDeleteObserver.class, int.class);
                IPackageDeleteObserver observer = new IPackageDeleteObserver.Stub() {
                    @Override
                    public void packageDeleted(String s, int i) throws RemoteException {
                        if (i == 1) {
                            if (listener != null)
                                listener.onSuccess();
                        } else {
                            if (listener != null)
                                listener.onError(new RuntimeException(s + " uninstall error"));
                        }
                    }
                };

                methodDeletePackage.invoke(mCtx.getPackageManager(), packageName, observer, 0);
            } catch (Throwable e) {
                if (listener != null)
                    listener.onError(e);
            }
        }


        public static void runInstall(Context mCtx, File archiveFile, final InstallListener listener) {

            int installFlags = 64; //  PackageManager.INSTALL_ALL_USERS
            installFlags |= 2;     // PackageManager.INSTALL_REPLACE_EXISTING;
            installFlags |= 128;   // PackageManager.INSTALL_ALLOW_DOWNGRADE;

            try {
                Uri uri = Uri.fromFile(archiveFile);
                Class<?> pio = Class.forName("android.app.PackageInstallObserver");
                Method methodInstallPackage = PackageManager.class.getDeclaredMethod("installPackage", Uri.class, pio, int.class, String.class);
                IPackageInstallObserver2 observer = new IPackageInstallObserver2.Stub() {

                    @Override
                    public void onUserActionRequired(Intent intent) throws RemoteException {

                    }

                    @Override
                    public void onPackageInstalled(String packageName, int returnCode, String msg, Bundle extras) {
                        if (packageName != null) {
                            if (listener != null)
                                listener.onSuccess();
                        } else {
                            if (listener != null)
                                listener.onError(new RuntimeException("install error"));
                        }
                    }
                };

                Object o = pio.newInstance();

                Field field = pio.getDeclaredField("mBinder");
                field.setAccessible(true);
                field.set(o, observer);
                field.setAccessible(false);

                methodInstallPackage.invoke(mCtx.getPackageManager(), uri, o, installFlags, null);
            } catch (Throwable e) {
                e.toString();
//                Log.i("sdk",e.toString());
                if (listener != null)
                    listener.onError(e);
            }
        }

    }
}
