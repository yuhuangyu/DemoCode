package com.example;

/**
 * Created by ASUS on 2018/6/13.
 */

public class States {
    public static void main(String[] args) {
        String str = "";
        char c;
        getCurrentChar(str);


    }

    private static char getCurrentChar(String str) {
        return  str.toCharArray()[currentNum++];
    }

    static int currentNum = 0;

    public void doNum(){

    }

}
