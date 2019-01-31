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

public class LocationUtil {
    private static Map<String, String> mapLocation = new HashMap<>();;
    private static LocationManager locationManager;

    /**
     * 获取位置信息
     * @return
     */
    public static Map<String, String> getLocation() {
        return mapLocation;
    }


    private static Map<String, String> saveLocation(Location location) {
        if(location == null)return null;
        mapLocation.put("longitude", String.valueOf(location.getLongitude()));
        mapLocation.put("latitude", String.valueOf(location.getLatitude()));
        remove();
        return mapLocation;
    }

    private static LocationListener locationListener = new LocationListener() {
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

    private static void remove() {
        if (locationManager != null && locationListener != null) {
            // 关闭程序时将监听器移除
            Log.e("sdk", "remove getLocation ");
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
        }
    }


    public static void initLocation(Context context) {
        Log.e("sdk", "init getLocation ");
        remove();
        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders( true );
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
        Location location = locationManager.getLastKnownLocation( locationProvider );
        if (location != null) {
            saveLocation(location);
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            Log.e("sdk", "== address "+address);
        }else {
            locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
            Location location2 = locationManager.getLastKnownLocation(locationProvider);
            if (location2 != null) {
                saveLocation(location);
                double latitude2 = location2.getLatitude();
                double longitude2 = location2.getLongitude();
                Log.e("sdk", "== address222 "+latitude2+" - "+longitude2);
            }
        }
    }
}
