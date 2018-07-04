package com.test.allandroidexamples.animations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(new MyView(this));
//        int i = 10;
//        IFunc iFunc
//        MyView myView = new MyView(this);
//        myView.setC
    }
}
