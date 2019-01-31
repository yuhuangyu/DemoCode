package com.api.utils.myfb;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.Map;


/**
 * Created by fj on 2018/8/14.
 */

public class App extends Application {

    private static final String AF_DEV_KEY = "uafFKK8uqbvUn8vGpKSYpZ";

    @Override
    public void onCreate() {
        super.onCreate();
       /* AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener() {

                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> map) {
                        Log.e("sdk", "onInstallConversionDataLoaded "+map.toString());
                    }

                    @Override
                    public void onInstallConversionFailure(String s) {
                        Log.e("sdk", "onInstallConversionFailure "+s);
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> map) {
                        Log.e("sdk", "onAppOpenAttribution "+map);
                    }

                    @Override
                    public void onAttributionFailure(String s) {
                        Log.e("sdk", "onAttributionFailure "+s);
                    }
                };
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
*/
    }
}
