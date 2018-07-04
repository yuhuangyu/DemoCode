package com.example;

import java.io.File;

/**
 * Created by ASUS on 2018/5/2.
 */

public class testbat {

    public static void rename(String oldPath, String newPath){
        //oldPath like "mnt/sda/sda1/我.png"
        File file = new File(oldPath);
        //newPath like "mnt/sda/sda1/我的照片.png"
        file.renameTo(new File(newPath));
    }

}
