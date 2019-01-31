package com.test.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.test.pushlive.LiveService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent service = new Intent(this,LiveService.class);
        this.startService(service);

        AccountHelper.addAccount(this);
        AccountHelper.autoSync();
    }

}
