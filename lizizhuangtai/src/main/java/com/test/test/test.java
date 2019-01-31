package com.test.test;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Printer;

import com.test.lizizhuangtai.R;

import java.util.*;


/**
 * Created by fj on 2018/8/9.
 */

public class test extends Activity {
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        /*defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (defaultUncaughtExceptionHandler != null) {
                    defaultUncaughtExceptionHandler.uncaughtException(t,e);
                }

            }
        });



        startTime();

        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                Log.e("sdk", "=== "+x);
                stopTimer();
                startTime();
            }
        });*/



//        String sss = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Optional.ofNullable(sss).map(s -> s.length()).orElseGet(null);
//        }

//        ButterKnife.bind(this);
    }

    private void startTime() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
                Log.e("sdk", "==="+allStackTraces);
            }
        }, 0, 20000);
    }

    // 停止定时器
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }
}
