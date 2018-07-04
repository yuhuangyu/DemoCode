package com.example.designpatterns.adapter;

/**
 * Created by ASUS on 2018/5/25.
 */

public class OtherAPIImpl implements OtherAPI {

    @Override
    public String dowork() {
        return new Adapter().dowork();
    }
}
