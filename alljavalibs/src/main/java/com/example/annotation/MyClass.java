package com.example.annotation;

import android.support.annotation.NonNull;
/*
*
* android.support.annotation路径下添加常用注解
* 无需导入 注解包 注解也有效
*
*
* */
public class MyClass {
    public static void main(String[] args) {
        String str = null;
//        光标放到调用方法的参数上有提示
        aaa(str);
    }

    public static void aaa(@NonNull String aaa) {

    }
}
