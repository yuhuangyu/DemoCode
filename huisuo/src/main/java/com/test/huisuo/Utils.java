package com.test.huisuo;


import android.app.IServiceConnection;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ASUS on 2018/3/28.
 */

public class Utils {
    /** Returns the The referrer url of the installed package. */
    private static final String KEY_INSTALL_REFERRER = "install_referrer";
    /** The timestamp in seconds when referrer click happens. */
    private static final String KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_seconds";
    /** The timestamp in seconds when installation begins. */
    private static final String KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_seconds";


    private static final String SERVICE_PACKAGE_NAME = "com.android.vending";
    private static final String SERVICE_NAME =
            "com.google.android.finsky.externalreferrer.GetInstallReferrerService";
    private static final String SERVICE_ACTION_NAME =
            "com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE";
    private static String referrer;
    private static String click_timestamp;
    private static String begin_timestamp;

    public static void replaceReferrer(Context context) {
        Hooker.hook();
        Hooker.addHocker(new Hooker.OnActivityManagerHooker() {
            @Override
            public void onBindService(Intent intent, final IServiceConnection conn) {
                Log.e("Sdk","==bindService "+intent.getAction());
                if (SERVICE_ACTION_NAME.equals(intent.getAction())) {
                    ComponentName componentName = new ComponentName(SERVICE_PACKAGE_NAME, SERVICE_NAME);
                    try {
                        conn.connected(componentName, (IBinder) Proxy.newProxyInstance(IBinder.class.getClassLoader(), new Class[]{IBinder.class}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
                                if ("transact".equals(method.getName())) {
                                    Log.e("Sdk", "==onBindService transact11 "+method.getName());
                                    if (objects[0] instanceof Integer) {
                                        int object0 = (int) objects[0];
                                        Parcel object1 = (Parcel) objects[1];
                                        Parcel object2 = (Parcel) objects[2];
                                        int object3 = (int) objects[3];
                                        Log.e("Sdk", "==onBindService transact11 "+object0);
                                        if (object0 == 1) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString(KEY_INSTALL_REFERRER, "utm_source=(not%20set)&amp;utm_medium=(not%20set)");
                                            bundle.putLong(KEY_REFERRER_CLICK_TIMESTAMP, 1523595661);
                                            bundle.putLong(KEY_INSTALL_BEGIN_TIMESTAMP, 1523599261);
                                            Bundle _result = bundle;
                                            object2.writeNoException();
                                            if(_result != null) {
                                                object2.writeInt(1);
                                                _result.writeToParcel(object2, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                                            } else {
                                                object2.writeInt(0);
                                            }
                                            objects[2] = object2;
                                            return method.invoke(new Binder(), objects);
                                        }
                                    }
                                }
                                return method.invoke(conn.asBinder(),objects);
                            }
                        }));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
