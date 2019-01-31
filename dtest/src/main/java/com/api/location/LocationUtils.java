package com.api.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fj on 2019/1/8.
 */

public class LocationUtils {
    private static Context mContext;
    private static LocationUtils mInstance;
    private static Map<String, String> mapLocation = new HashMap<>();;
    private static LocationManager locationManager1;

    private LocationUtils(Context mContext) {
        this.mContext = mContext;
//        initLocation();
        getLocation(mContext);
    }

    public static LocationUtils getInstance(Context context){
        if (mInstance == null){
            synchronized (LocationUtils.class){
                if (mInstance == null){
                    mInstance = new LocationUtils(context);
                    mContext = context;

                }
            }
        }
        return mInstance;
    }

    /*static LocationManager locationManager;
    static Location location;
    static String provider;

    public static void initLocation() {

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
//            Toast.LENGTH_SHORT).show();
//            LogUtil.d(TAG,"No location provider to use");
            return;
        }

        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // 显示当前设备的位置信息
            saveLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);

    }*/

    /**
     * 获取位置信息
     * @return
     */
    public static Map<String, String> getLocation() {
        return mapLocation;
    }


    public static Map<String, String> saveLocation(Location location) {
        if(location == null)return null;
        mapLocation.put("longitude", String.valueOf(location.getLongitude()));
        mapLocation.put("latitude", String.valueOf(location.getLatitude()));
        remove();
        return mapLocation;
    }

    public static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            Log.e("sdk", "== onLocationChanged address "+address);
            // 更新当前设备的位置信息
            saveLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public static void remove() {
        if (locationManager1 != null) {
            // 关闭程序时将监听器移除
            locationManager1.removeUpdates(locationListener);
        }
    }


    private void getLocation(Context context) {
        Log.e("sdk", "init getLocation ");
        //1.获取位置管理器
        locationManager1 = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager1.getProviders( true );
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
        Location location = locationManager1.getLastKnownLocation( locationProvider );
        if (location != null) {
            saveLocation(location);
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            Log.e("sdk", "== address "+address);
        }else {
            locationManager1.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
            Location location2 = locationManager1.getLastKnownLocation(locationProvider);
            if (location2 != null) {
                saveLocation(location);
                double latitude2 = location2.getLatitude();
                double longitude2 = location2.getLongitude();
                Log.e("sdk", "== address222 "+latitude2+" - "+longitude2);
            }
        }
    }
}
