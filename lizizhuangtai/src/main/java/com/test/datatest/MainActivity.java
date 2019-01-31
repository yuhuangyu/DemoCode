package com.test.datatest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FileCacheStore.get(this).put("onTouch", "111,222");
        FileCacheStore.get(this).put("onTouch", FileCacheStore.get(this).get("onTouch","")+"111,222,111,444");
        FileCacheStore.get(this).put("bbb", "111,222,333");

        String aaa = FileCacheStore.get(this).get("onTouch", "");
        String bbb = FileCacheStore.get(this).get("bbb", "");
        Log.e("Sdk", aaa+"---"+bbb);
    }

}
