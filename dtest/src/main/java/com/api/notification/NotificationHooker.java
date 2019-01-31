package com.api.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by anye6488 on 2016/8/19.
 * 监测状态栏类
 */
public class NotificationHooker {
    private <T> T getHookObject(ClassLoader loader, final Object obj, final InvocationHandler hock, Class<?>... inters) {
        return (T) Proxy.newProxyInstance(loader, inters, new InvocationHandler() {
            private Class<?> _hookClass = hock.getClass();

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return hock.invoke(obj, method, args);
            }
        });
    }

    /*//发送指定广播
    private static void sendSpeBroadcast(final Context context, final String type) {
        StateUtil.save(context, type, "isApkAd", true);
        Intent speBroadcast = new Intent("com.sdk.inner.download");
        speBroadcast.setPackage(type);
        speBroadcast.putExtra("type", type);
        context.sendBroadcast(speBroadcast);
        Logger.e("发送" + type + "广播完成");
    }*/

    private Object getHookNotificationManager(final Context context, Object m) {
        return getHookObject(context.getClassLoader(), m, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("enqueueNotificationWithTag".equals(method.getName())) {
                    Integer inId = null;
                    boolean isReturn = false;
                    int[] idOut = null;
                    int i = 0;
                    for (Object v : args) {
                        i++;
                        Log.e("sdk","================"+v);
                        /*if (v instanceof Integer) {
                            Integer id = (Integer) v;
                            if (inId == null)
                                inId = id;
                            if (id == 10091 || id == 10092 || (i == 4 && id == 0))//百度apk广告
                            {
                                args[0] = PackageAssist.getRealPkg();
                                Intent intent = new Intent("notificationProxy");
                                intent.putExtra("notificationParam", PendingIntentValue.getIntent());
                                intent.putExtra("notificationIsServer", PendingIntentValue.isServer());
                                int requestCode = Integer.parseInt(PendingIntentValue.getCreateTime().substring(6));
                                if (Build.VERSION.SDK_INT >= 23) {
                                    Field field = ((Notification) args[4]).getSmallIcon().getClass().getDeclaredField("mString1");
                                    field.setAccessible(true);
                                    field.set((Icon) (((Notification) args[4]).getSmallIcon()), PackageAssist.getRealPkg());
                                }
                                ((Notification) args[4]).contentIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                isReturn = true;
                                boolean isSendAsApk = StateUtil.getBoolean(context, "One", "isSendAsApk");
                                boolean isClick = StateUtil.getBoolean(context, "One", "isClick");
//                                Logger.i("下载变化");
                                //监测到状态出现百度Apk广告下载，发送广播
                                if (!isSendAsApk && isClick) {
                                    sendSpeBroadcast(context, "bd");

                                    Logger.showOnUI(context, "当前apk广告点击有效");
                                    Logger.e("发送有效广播(apk)");
                                    StateUtil.save(context, "One", "isSendAsApk", true);
                                    StateUtil.save(context, "One", "isClick", false);
                                }
                            }
                        } else if (v instanceof String) {
                            if (!context.getPackageName().equals(v)) {
                                String tag = (String) v;
                                if (tag.startsWith("GDT_DOWNLOAD_NOTIFI_TAG")) {
                                    args[0] = PackageAssist.getRealPkg();
                                    Intent intent = new Intent("notificationProxy");
                                    intent.putExtra("notificationParam", PendingIntentValue.getIntent());
                                    intent.putExtra("notificationIsServer", PendingIntentValue.isServer());
                                    int requestCode = Integer.parseInt(PendingIntentValue.getCreateTime().substring(6));
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        Field field = ((Notification) args[4]).getSmallIcon().getClass().getDeclaredField("mString1");
                                        field.setAccessible(true);
                                        field.set((Icon) (((Notification) args[4]).getSmallIcon()), PackageAssist.getRealPkg());
                                    }
                                    ((Notification) args[4]).contentIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                    Logger.i("下载变化");
                                    isReturn = true;
                                    boolean isSendAsApk = StateUtil.getBoolean(context, "Two", "isSendAsApk");
                                    boolean isClick = StateUtil.getBoolean(context, "Two", "isClick");
                                    //监测到状态出现百度Apk广告下载，发送广播
                                    if (!isSendAsApk && isClick) {
                                        sendSpeBroadcast(context, "gdt");
                                        Logger.showOnUI(context, "当前apk广告点击有效");
                                        Logger.e("发送有效广播(apk)");
                                        StateUtil.save(context, "Two", "isSendAsApk", true);
                                        StateUtil.save(context, "Two", "isClick", false);
                                    }
                                }
                            }
                        } else if (v instanceof int[]) {
                            idOut = (int[]) v;
                        }*/
                    }
//                    if (isReturn)
//                    {
//                        if (idOut != null)
//                            idOut[0] = inId;
//
//                        //return null;
//                    }
                }
                return method.invoke(proxy, args);
            }
        }, m.getClass().getInterfaces());
    }

    public void hookNotification(Context context) {
        try {
            NotificationManager mm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mm.cancel(0);
            Reflect.set(NotificationManager.class, "sService", getHookNotificationManager(context, Reflect.get(NotificationManager.class, "sService")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hookNotificationManager(final Context context) throws Exception {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Method getService = NotificationManager.class.getDeclaredMethod("getService");
        getService.setAccessible(true);
        // 第一步：得到系统的 sService
        final Object sOriginService = getService.invoke(notificationManager);

        Class iNotiMngClz = Class.forName("android.app.INotificationManager");
        // 第二步：得到我们的动态代理对象
        Object proxyNotiMng = Proxy.newProxyInstance(context.getClass().getClassLoader(), new
                Class[]{iNotiMngClz}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.d("sdk", "invoke(). method:" + method);
                String name = method.getName();
                Log.d("sdk", "invoke: name=" + name);
                if (args != null && args.length > 0) {
                    for (Object arg : args) {
                        Log.d("sdk", "invoke: arg=" + arg);
                    }
                }
                Toast.makeText(context.getApplicationContext(), "检测到有人发通知了", Toast.LENGTH_SHORT).show();
                // 操作交由 sOriginService 处理，不拦截通知
                return method.invoke(sOriginService, args);
                // 拦截通知，什么也不做
                //                    return null;
                // 或者是根据通知的 Tag 和 ID 进行筛选
            }
        });
        // 第三步：偷梁换柱，使用 proxyNotiMng 替换系统的 sService
        Field sServiceField = NotificationManager.class.getDeclaredField("sService");
        sServiceField.setAccessible(true);
        sServiceField.set(notificationManager, proxyNotiMng);

    }

}
