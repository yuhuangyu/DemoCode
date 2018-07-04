package com.example.designpatterns.adapter;

/**
 * Created by ASUS on 2018/5/25.
 */

public class System implements SystemAPI{

    @Override
    public String dowork() {
        return "this is System";
    }
}
