package com.test.markdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.test.markdemo.exercise.webviewtest.WebManager;

/**
 * Created by ASUS on 2017/10/27.
 */

public class Mservice extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WebManager webManager = new WebManager(this,null);
        webManager.setup("","https://www.baidu.com/");
        return super.onStartCommand(intent, flags, startId);
    }
}
