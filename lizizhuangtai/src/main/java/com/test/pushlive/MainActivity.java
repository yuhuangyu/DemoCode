package com.test.pushlive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by fj on 2018/8/6.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        startService(MainService.getIntentStart(this));
        Intent service = new Intent(this,LiveService.class);
        this.startService(service);
    }
}
