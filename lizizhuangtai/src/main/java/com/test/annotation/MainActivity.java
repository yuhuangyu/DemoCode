package com.test.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.api.utils.annotationprocessor.Interface;
import com.api.utils.annotationprocessor.annot.AntiBean;


public class MainActivity extends Activity {
    @AntiBean
    private int age = 111;
    @AntiBean
    private String nane = "onTouch";

    private String hggg = "bbb";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getUserName();
//        age = 110000;

//        age = 11111;
    }

    @AntiBean
    public void getUserName(){
        int a= 113;
        hggg = "aaaa";
//        int b = a+1;
//        System.out.println("== "+b);
    }


    public void getUserName22(){
        int b= 11;
    }

    @Interface("ManInterface")
    public void eat() {
        System.out.println("Eat");
    }

    @Interface("ManInterface")
    public void eat2() {}

}
