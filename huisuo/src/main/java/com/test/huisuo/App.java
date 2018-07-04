package com.test.huisuo;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.Map;

//import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by ASUS on 2018/3/22.
 */

public class App extends Application {
    private static final String AF_DEV_KEY = "A4wdhyC7ZJbrrucaimwHtN";
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.replaceReferrer(this);
//        CrashReport.initCrashReport(getApplicationContext(), "03d4b5a0dd", false);
        AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener() {

            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                Log.e("Sdk", "==onInstallConversionDataLoaded");
//                for (String in : map.keySet()) {
//                    //map.keySet()返回的是所有key的值
//                    String str = map.get(in);//得到每个key多对用value的值
//                    Log.e("Sdk", "== "+in + "     " + str);
//                }
            }

            @Override
            public void onInstallConversionFailure(String s) {
                Log.e("Sdk", "==onInstallConversionFailure");
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                Log.e("Sdk", "==onAppOpenAttribution");
            }

            @Override
            public void onAttributionFailure(String s) {
                Log.e("Sdk", "==onAttributionFailure");
            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionDataListener,getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
//        AppsFlyerLib.getInstance().startTracking(this,AF_DEV_KEY);

    }
}
