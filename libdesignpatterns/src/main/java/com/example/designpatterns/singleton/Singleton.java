package com.example.designpatterns.singleton;

/**
 * Created by ASUS on 2018/5/25.
 */

public class Singleton {
    private static Singleton singleton;
    private Singleton(){}

    public static Singleton get(){
        if (singleton ==null){
            synchronized (Singleton.class){
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
