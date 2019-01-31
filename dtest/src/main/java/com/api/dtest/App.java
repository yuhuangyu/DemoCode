package com.api.dtest;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

import com.api.hock.AdvertisingIdClient;
import com.appsflyer.AppsFlyerLib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by fj on 2018/12/13.
 */

public class App extends Application {

    private static final String AF_DEV_KEY = "A4wdhyC7ZJbrrucaimwHtN";

    @Override
    public void onCreate() {
        super.onCreate();
//        AppsFlyerLib.getInstance().startTracking(this,AF_DEV_KEY);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        hockAdid(base);
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
                        Log.e("sdk","====id "+id);

                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }
}
