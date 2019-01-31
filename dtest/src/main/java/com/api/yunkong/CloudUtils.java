package com.api.yunkong;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.api.dtest.MainActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by fj on 2018/12/25.
 */

public class CloudUtils {

    private long Gms_version = 0;
    private long Gp_version = 0;
    private long gsf_version = 0;
    private long Gsf_login_version = 0;
    private long gp_game_version = 0;
    private Context context;
    private String dwonloadUrl;
    private String referrer;
    private long fileSize;

    private long loc_mcc = 0;
    private long loc_mnc = 0;
    private long loc_cid = 0;
    private long loc_lac = 0;
    private double loc_lat = 0;
    private double loc_lng = 0;
    private String advertisingId;

    public CloudUtils(Context context, String dwonloadUrl, String referrer){
        this.context = context;
        this.dwonloadUrl = dwonloadUrl;
        this.referrer = referrer;
//        this.fileSize = getLength(dwonloadUrl);
        getAdid(context);
        getGoogleVersionCode();
        getLocation();
        getTelephony();
    }



    private int getLength(String urll) {
        HttpURLConnection connection = null;
        try {
            //连接网络
            URL url = new URL(urll);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(5000);
            int length = -1;
            if (connection.getResponseCode() == 200) {//网络连接成功
                //获得文件长度
                length = connection.getContentLength();
            }
            if (length <= 0) {
                //连接失败
                return -1;
            }
            return length;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            try {
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void getGoogleVersionCode() {

        PackageManager pckMan = context.getPackageManager();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);
        for (PackageInfo pInfo : packageInfo) {
            if ("com.android.vending".equals(pInfo.packageName)) {
                Gp_version = pInfo.versionCode;
            } else if ("com.google.android.gms".equals(pInfo.packageName)) {
                Gms_version = pInfo.versionCode;
            } else if ("com.google.android.stf".equals(pInfo.packageName)) {
                gsf_version = pInfo.versionCode;
            } else if ("com.google.android.stf.login".equals(pInfo.packageName)) {
                Gsf_login_version = pInfo.versionCode;
            } else if ("com.google.android.play.games".equals(pInfo.packageName)) {
                gp_game_version = pInfo.versionCode;
            }
        }
    }


    public void invokeInstall() {

        TaskBean taskBean = new TaskBean("admob", "aid",
                getAdroidId(context), getGAID(), getBluetoothMac(context), referrer, dwonloadUrl, fileSize,
                Gms_version,  Gp_version,  gsf_version, Gsf_login_version, gp_game_version,
                loc_mcc, loc_mnc, loc_lng, loc_cid, loc_lac, loc_lat);

        Log.e("sdk","===taskBean  "+taskBean.toString());
    }


    private String getGAID(){
        return MainActivity.advertisingId;
    }

    protected void getAdid(final Context base) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AdvertisingIdClient.AdInfo adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(base);
                    if (adInfo != null) {
                        advertisingId = adInfo.getId();
                        Log.e("sdk", "AdvertisingId====id "+ advertisingId);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }

    private String getBluetoothMac(Context context){
        String macAddress = null;
        try {
            macAddress = Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    private String getAdroidId(Context context){
        String androidId_key;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            androidId_key= Settings.Secure.ANDROID_ID;
        }else{
            androidId_key= Settings.System.ANDROID_ID;
        }
        return Settings.System.getString(context.getContentResolver(), androidId_key);
    }

    private void getLocation() {
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders( true );
        String locationProvider = null;
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
//            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            loc_lat = location.getLatitude();
            loc_lng= location.getLongitude();
        }
    }

    private void getTelephony(){
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回值MCC + MNC
        String operator = mTelephonyManager.getNetworkOperator();
        try {
            if (operator != null && operator.length()>3) {
                loc_mcc = Integer.parseInt(operator.substring(0, 3));
                loc_mnc = Integer.parseInt(operator.substring(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CellLocation cel = null;
        try {
            cel = mTelephonyManager.getCellLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(cel != null && cel instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
            loc_lac = gsmCellLocation.getLac();
            loc_cid = gsmCellLocation.getCid();
        } else if (cel != null && cel instanceof CdmaCellLocation) {

            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
//            int sid = cdmaCellLocation.getSystemId();
            loc_lac = cdmaCellLocation.getNetworkId();
            loc_cid = cdmaCellLocation.getBaseStationId();

        }


    }

    private int calcDay(long startTime) {
//        return DateUtil.getDay(System.currentTimeMillis() - startTime);
        return (int) ((System.currentTimeMillis() - startTime)/(24*60*60*1000));
    }

    public static class TaskBean{
        private String cid;
        private String pkg;
        private String android;
        private String adid;
        private String bluetoolmac;
        private String Referrer;
        private String dwonloadUrl;
        private long fileSize;

        private long Gms_version;
        private long Gp_version;
        private long gsf_version;
        private long Gsf_login_version;
        private long gp_game_version;

        private long loc_mcc;
        private long loc_mnc;
        private long loc_cid;
        private long loc_lac;
        private double loc_lng;
        private double loc_lat;

        public TaskBean(String cid, String pkg, String android, String adid,
                        String bluetoolmac, String Referrer, String dwonloadUrl,
                        long fileSize, long Gms_version, long Gp_version, long gsf_version,
                        long Gsf_login_version, long gp_game_version,
                        long loc_mcc, long loc_mnc, double loc_lng,
                        long loc_cid, long loc_lac, double loc_lat){
            this.cid = cid;
            this.pkg = pkg;
            this.android = android;
            this.adid = adid;
            this.bluetoolmac = bluetoolmac;
            this.Referrer = Referrer;
            this.dwonloadUrl = dwonloadUrl;
            this.fileSize = fileSize;

            this.Gms_version = Gms_version;
            this.Gp_version = Gp_version;
            this.gsf_version = gsf_version;
            this.Gsf_login_version = Gsf_login_version;
            this.gp_game_version = gp_game_version;

            this.loc_mcc = loc_mcc;
            this.loc_mnc = loc_mnc;
            this.loc_lng = loc_lng;
            this.loc_cid = loc_cid;
            this.loc_lac = loc_lac;
            this.loc_lat = loc_lat;
        }

        @Override
        public String toString() {
            return "TaskBean  cid: "+cid+" ,pkg: "+pkg+" ,android: "+android+" ,adid: "+adid
                    +" ,bluetoolmac: "+bluetoolmac+" ,Referrer: "+Referrer+" ,dwonloadUrl: "+dwonloadUrl
                    +" ,fileSize: "+fileSize+" ,Gms_version: "+Gms_version+" ,Gp_version: "+Gp_version
                    +" ,gsf_version: "+gsf_version+" ,Gsf_login_version: "+Gsf_login_version+" ,gp_game_version: "+gp_game_version
                    +" ,loc_mcc: "+loc_mcc+" ,loc_mnc: "+loc_mnc+" ,loc_lng: "+loc_lng
                    +" ,loc_cid: "+loc_cid+" ,loc_lac: "+loc_lac+" ,loc_lat: "+loc_lat;
        }
    }
}
