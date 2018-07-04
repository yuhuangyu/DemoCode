package com.example.designpatterns.observer;

/**
 * Created by ASUS on 2018/5/25.
 */

public class Weather {
    private String description;

    public Weather(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void show(){
        System.out.println("Weather: "+description);
    }

}
