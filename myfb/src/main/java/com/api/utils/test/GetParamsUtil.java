package com.api.utils.test;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class GetParamsUtil {

    private Context mContext;

    private SensorManager mSensorManager = null;
    private SensorEventListener mGyroscopeSensorListener = null;

    GetParamsUtil(Context context) {
        this.mContext = context;
    }


    int getGPVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
//            Lg.e("getGPVersion ", e);
        }
        return code;
    }

    boolean is_Simulator() {
        return notHasBlueTooth() || notHasLightSensorManager(mContext);
    }

    /**
     * 判断蓝牙是否有效来判断是否为模拟器
     *
     * @return true 为模拟器
     */
    boolean notHasBlueTooth() {
        try {
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            if (ba == null) {
                return true;
            } /*else {
                // 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
                String name = ba.getName();
                return TextUtils.isEmpty(name);
            }*/
        } catch (Exception e) {
            Log.e("notHasBlueTooth ", e.toString());
        }
        //没有蓝牙权限时，默认不是虚拟机
        return false;
    }

    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     *
     * @return true 为模拟器
     */
    Boolean notHasLightSensorManager(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor8 = null; //光
        if (sensorManager != null) {
            sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        return null == sensor8;
    }

    /**
     * 是否存在蓝牙模块
     *
     * @return true 设备中存在蓝牙模块
     */
    boolean isBluetoothExist() {
        try {
            boolean granted = true;
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                            1);
//                } else {
//
//                }
//            }
//            if (!granted) {
//                return false;
//            }
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (null == bluetoothAdapter) {
                return false;
            }/* else {
                return !TextUtils.isEmpty(bluetoothAdapter.getName());
            }*/
        } catch (Exception e) {
            Log.e("isBluetoothExist ", e.toString());
        }
        return true;
    }

    /**
     * 是否有蓝牙绑定设备
     *
     * @return true 设备绑定了大于0的蓝牙设备
     */
    /*boolean hasBluetoothBind() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            // If there are paired devices
            return pairedDevices.size() > 0;
        }
        return false;
    }*/

    /**
     * 是否有光线传感器
     *
     * @return true 设备存在光线传感器
     */
    boolean isLightSensorExist() {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = null;
        if (null != sensorManager) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); // 获取光线传感器
        }
        return null != lightSensor;
    }

    /**
     * 是否有加速度传感器
     *
     * @return true 设备存在加速度传感器
     */
    boolean isAccelerometerExist() {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = null;
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 获取加速度传感器
        }
        return null != accelerometer;
    }

    /**
     * 判断是否开启了开发者模式
     *
     * @return true 开启了开发者模式
     */
    boolean isDevelopmentEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return (Settings.Secure.getInt(mContext.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) > 0);
        } else {
            return (Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) > 0);
        }
    }

    /**
     * 设备是否获取root权限
     *
     * @return true 设备获取了root权限
     */
    boolean isRootEnabled() {
        boolean flag_1 = false;
        String isRoot_1 = Build.TAGS;
        flag_1 = null != isRoot_1 && isRoot_1.contains("test-keys");

        boolean flag_2 = false;
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                flag_2 = true;
            }
        }
        return flag_1 || flag_2;
    }

    /**
     * 是否通过数据线连接usb调试
     *
     * @return true 设备使用数据线连接了usb调试
     */
    boolean isLinkUSB() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = mContext.registerReceiver(null, ifilter);
        //如果设备正在充电，可以提取当前的充电状态和充电方式（无论是通过 USB 还是交流充电器），如下所示：

        // Are we charging / charged?
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (isCharging) {
            if (usbCharge) {
//                Toast.makeText(mContext, "手机正处于USB连接！", Toast.LENGTH_SHORT).show();
                return true;  //连接usb且在调试状态
            } else if (acCharge) {
//                Toast.makeText(mContext, "手机通过电源充电中！", Toast.LENGTH_SHORT).show();
                return false;  //连接usb在充电
            }
        } else {
//            Toast.makeText(mContext, "手机未连接USB线！", Toast.LENGTH_SHORT).show();
            return false;   //未连接usb
        }
        return true;
    }

    /**
     * 是否开启了usb调试模式
     *
     * @return true 设备开启了usb调试模式
     */
    boolean isUsbDebugEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return (Settings.Secure.getInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0);
        } else {
            return (Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
        }
    }

    /**
     * 是否存在解锁密码
     *
     * @return true 设备开启了锁屏密码
     */
    boolean hasLockPwd() {
        boolean isLock = false;
        if (Build.VERSION.SDK_INT >= 23) {
            KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
            isLock = keyguardManager.isKeyguardSecure();
        } else {
            isLock = Settings.System.getInt(
                    mContext.getContentResolver(), Settings.System.LOCK_PATTERN_ENABLED, 0) == 1;
        }
        return isLock;
    }

    /**
     * 判断设备 是否使用代理上网
     *
     * @return true 使用代理上网
     */
    boolean isWifiProxy() {
        // 是否大于等于4.0
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(mContext);
            proxyPort = android.net.Proxy.getPort(mContext);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 设备是否开启了VPN
     *
     * @return true 设备安开启了VPN
     */
    boolean isOpenVPN() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // vpn 开启
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("isOpenVPN ", e.toString());
        }
        return false;
    }

    /**
     * 设备上是否有摄像头
     *
     * @return true 设备有摄像头
     */
    boolean isCameraExist() {
        int cameraCount = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cameraCount = Camera.getNumberOfCameras();
        } else {
            CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] camera = cameraManager.getCameraIdList();
                if (camera != null) {
                    cameraCount = camera.length;
                }
            } catch (CameraAccessException e) {
                Log.e("isCameraExist", e.toString());
            }
        }
        return cameraCount > 0;
    }

    /**
     * 设备中是否包含Google账号或Facebook账号
     *
     * @return true 包含其中之一账号
     */
    boolean getAccount() {
        if (Build.VERSION.SDK_INT >= 26) {
            //8.0以上设备需要startActivityForResult弹窗选择账号之后才能获取到account信息，暂时不用
//            Intent googlePicker = AccountManager.newChooseAccountIntent(null, null,
//                    null, true, null, null, null, null);
//            startActivityForResult(googlePicker, 1);
            return true;
        } else {
            /*AccountManager accountManager = AccountManager.get(mContext);
            Account[] accounts = accountManager.getAccounts();
            String type = "";
            for (Account account : accounts) {
//                Lg.d("account.name=" + account.name);
//                Lg.d("account.type=" + account.type);
                type = account.type.toLowerCase();
                if (type.contains("com.google") || type.contains("facebook")) {
                    return true;
                }
            }*/
//            Lg.d("hongyan:accounts.length=" + accounts.length);
            return false;
        }
    }

    /**
     * 设备编译版本是否是debug
     *
     * @return true debug版本
     */
    boolean isDebugSystem() {
        String version = "";
        try {
            Method method = Build.class.getDeclaredMethod("getString", String.class);
            method.setAccessible(true);
            version = (String) method.invoke(new Build(), "ro.build.type");
            version.toLowerCase();
        } catch (NoSuchMethodException e) {
//            Lg.e("isDebugSystem ", e);
        } catch (InvocationTargetException e) {
//            Lg.e("isDebugSystem ", e);
        } catch (IllegalAccessException e) {
//            Lg.e("isDebugSystem ", e);
        }
        if (version.equals("userdebug") || version.contains("debug")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取设备当前电量
     *
     * @return 90 电量90%
     */
    int getCurBattery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
            int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//            Lg.d("battery = " + battery);
            return battery;
        } else {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryIntent = mContext.registerReceiver(null, intentFilter);
            if (null != batteryIntent) {
                int rst = batteryIntent.getIntExtra("level", 0);
//                Lg.d("battery level = " + rst);
                return rst;
            }
            return 100;
        }
    }

    /**
     * 设备中debug版本的apk的数量
     *
     * @return debug版本apk的数量
     */
    int getDebugApkCount() {
        PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

        int count = 0;
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            boolean rst = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (rst) {
                count++;
            }
        }
        return count;
    }

    /**
     * 是否有电话功能（判断不准，模拟器也返回true）
     *
     * @return true 有电话功能
     */
    boolean hasTelephone() {
        TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否存在基带版本
     *
     * @return true 没有基带版本
     */
    boolean notBasebandExist() {
        String value = "";
        try {
            Object roSecureObj = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class)
                    .invoke(null, "gsm.version.baseband");
            if (roSecureObj != null) {
                value = (String) roSecureObj;
            }
        } catch (Exception e) {
//            Lg.e("notBasebandExist ", e);
            value = null;
        }
//        Lg.d("baseband = " + value);
        if (TextUtils.isEmpty(value)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 注册陀螺仪监听
     */
    void getGyroscopeListener() {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        }
        final Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                    Lg.d("方位角：" + event.values[0]);
//                    Lg.d("俯仰角：" + event.values[1]);
//                    Lg.d("横滚角：" + event.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//                Lg.d("onAccuracyChanged");
            }
        };
        mSensorManager.registerListener(mGyroscopeSensorListener, sensor, Sensor.TYPE_GYROSCOPE);
    }

    /**
     * 移除陀螺仪监听
     */
    void releaseGyroscopeListener() {
        if (mSensorManager != null && mGyroscopeSensorListener != null) {
            mSensorManager.unregisterListener(mGyroscopeSensorListener);
        }
        mSensorManager = null;
        mGyroscopeSensorListener = null;
    }

    /**
     * 是否是模拟器
     *
     * @return true 是模拟器
     */
    boolean isSimulator() {
        boolean isSimlator_1 = isNonRoutineCpu() || isBluestack() || isHook()
                || notBasebandExist();
        return isSimlator_1;
    }

    /**
     * @return
     */
    boolean isNonRoutineCpu() {
        String cpuInfo = readCpuInfo();
        if ((cpuInfo.contains("intel") || cpuInfo.contains("amd")))
            return true;
        else
            return false;
    }

    private String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
//            Lg.e("read cpu ", ex);
        }
        return result;
    }

    boolean isBluestack() {
        String[] known_bluestacks = {"/data/app/com.bluestacks.appmart-1.apk", "/data/app/com.bluestacks.BstCommandProcessor-1.apk",
                "/data/app/com.bluestacks.help-1.apk", "/data/app/com.bluestacks.home-1.apk", "/data/app/com.bluestacks.s2p-1.apk",
                "/data/app/com.bluestacks.searchapp-1.apk", "/data/bluestacks.prop", "/data/data/com.androVM.vmconfig",
                "/data/data/com.bluestacks.accelerometerui", "/data/data/com.bluestacks.appfinder", "/data/data/com.bluestacks.appmart",
                "/data/data/com.bluestacks.appsettings", "/data/data/com.bluestacks.BstCommandProcessor", "/data/data/com.bluestacks.bstfolder",
                "/data/data/com.bluestacks.help", "/data/data/com.bluestacks.home", "/data/data/com.bluestacks.s2p", "/data/data/com.bluestacks.searchapp",
                "/data/data/com.bluestacks.settings", "/data/data/com.bluestacks.setup", "/data/data/com.bluestacks.spotlight", "/mnt/prebundledapps/bluestacks.prop.orig"
        };
        for (int i = 0; i < known_bluestacks.length; i++) {
            String file_name = known_bluestacks[i];
            File qemu_file = new File(file_name);
            if (qemu_file.exists()) {
                return true;
            }
        }
        return false;
    }

    boolean isHook() {
        if (findHookAppName() || findHookAppFile() || findHookStack()) {
            return true;
        }
        return false;
    }

    private boolean findHookAppName() {
        PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (applicationInfo.packageName.equals("de.robv.android.xposed.installer")) {
                return true;
            }
            if (applicationInfo.packageName.equals("com.saurik.substrate")) {
                return true;
            }
        }
        return false;
    }

    private static boolean findHookAppFile() {
        try {
            Set<String> libraries = new HashSet<String>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            reader.close();
            for (String library : libraries) {
                if (library.contains("com.saurik.substrate")) {
                    return true;
                }
                if (library.contains("XposedBridge.jar")) {
                    return true;
                }
            }
        } catch (Exception e) {
//            Lg.e("findHookAppFile ", e);
        }
        return false;
    }

    private boolean findHookStack() {
        try {
            throw new Exception("findhook");
        } catch (Exception e) {
            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
                        return true;
                    }
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2")
                        && stackTraceElement.getMethodName().equals("invoked")) {
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("main")) {
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
                    return true;
                }
            }
        }
        return false;
    }


}
