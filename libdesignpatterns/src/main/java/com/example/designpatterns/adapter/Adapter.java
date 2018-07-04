package com.example.designpatterns.adapter;

/**
 * Created by ASUS on 2018/5/25.
 */

public class Adapter implements OtherAPI{

    @Override
    public String dowork() {

        String dowork = new System().dowork();
        return dowork.replace("System","Adapter");
    }
}
