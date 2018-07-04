package com.example.designpatterns.adapter;

import java.lang.*;
import java.lang.System;

public class myClass {
    public static void main(String[] args) {
        OtherAPIImpl otherAPI = new OtherAPIImpl();
        String dowork = otherAPI.dowork();
        System.out.println("=== "+dowork);
    }
}
