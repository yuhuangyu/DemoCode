package com.example;

/**
 * Created by fj on 2018/7/12.
 */

public class TestObject {
    public TestObject(){
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < 10000; i++) {
            stringBuilder.append('a');
        }
        String num = "";
        for (int i = 0; i < 1000; i++) {
            num = num+i+"";
        }
        stringBuilder.append(num);
    }
}
