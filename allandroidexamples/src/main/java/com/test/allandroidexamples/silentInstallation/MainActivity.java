package com.test.allandroidexamples.silentInstallation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/*
* 静默安装
*
*
* 手机获取root权限
* 清单文件配置权限：android.permission.INSTALL_PACKAGES
* app放入 system/PRIV-APP 目录
*
* */


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        MainActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        );
        super.onCreate(savedInstanceState);

        String path = Environment.getExternalStorageDirectory().getPath() + "/test/app11.apk";

        File file = new File(path);
        Log.e("sdk", "-- "+path+"  -- "+file.exists());
        AppManager.addPackage(this, file, new AppManager.InstallListener() {
            @Override
            public void onSuccess() {
                Log.e("sdk", "-onSuccess- ");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("sdk", "-onError- "+e.toString());
                if (e != null) {
                    Log.e("sdk", "-onError- "+e.getMessage());
                }
            }
        });
    }
}
