package com.annotation;
// NonNull的路径必须是这个
import android.support.annotation.NonNull;


/**
 * Created by fj on 2018/7/24.
 */

public class main {
    public static void main(String[] args) {
        String str = null;
        aaa(str);
    }


    public static void aaa(@NonNull String aaa) {

    }


}
