package com.test.lizizhuangtai;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.api.utils.annotationprocessor.CompileAnnotation;
import com.api.utils.annotationprocessor.Interface;

import java.io.IOException;

/**
 * Created by fj on 2018/8/2.
 */

public class MainActivity extends Activity {
    private Thread thread;
    //    @CompileAnnotation(value = 200)
//    private TextView tv_text;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity);
//        tv_text = (TextView) findViewById(R.id.tv);
        Button bbt = (Button)findViewById(R.id.bbt);

        //                    String s = RootCmd.execRootCmd("adb logcat -f /sdcard/test/log.txt");
//                    Process process = Runtime.getRuntime().exec("adb logcat -f /sdcard/test/log.txt");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = RootCmd.execRootCmd("logcat sdk:I *:S > /sdcard/test/a.log");
//                    String s = RootCmd.execRootCmd("adb logcat -f /sdcard/test/log.txt");
//                    Process process = Runtime.getRuntime().exec("adb logcat -f /sdcard/test/log.txt");
                    Log.e("sdk", "============  process " + s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        bbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                }
            }
        });
        Log.e("sdk","============222222222222");
        Log.e("sdk","============333333333333");

        /*Class<?> c = null;
        try {
            c = Class.forName("com.test.lizizhuangtai.ManInterface");
            Log.e("sdk","============"+c.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ManInterface manInterface = new ManInterface() {
            @Override
            public void eat() {
                System.out.println("Eat 111111111");
            }

            @Override
            public void eat2() {
                System.out.println("Eat 2222222222222");
            }
        };
        manInterface.eat();*/

        RootCmd.haveRoot();
        RootCmd.getInfo();
    }
//    @Interface("ManInterface")
//    public void eat() {
//        System.out.println("Eat");
//    }
//
//    @Interface("ManInterface")
//    public void eat2() {}
}
