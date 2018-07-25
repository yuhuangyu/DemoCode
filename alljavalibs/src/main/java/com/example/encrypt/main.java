package com.example.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by fj on 2018/7/24.
 */

public class main {
    public static void main(String[] args) {

        try {
            //解密
//            InputStream inputStream = new FileInputStream(new File("D:\\encry\\MidSmA"));
//            OutputStream outputStream = new FileOutputStream(new File("D:\\encry\\MidSmA_jiemi"));
            //ConstantUtil.writeFile(inputStream, outputStream, 379);

            //加密  文件夹中 要只有加密文件
            ConstantUtil.encrypt(new FileInputStream(new File("D:\\encry\\test\\MidSmA_jiemi")),new FileOutputStream(new File("D:\\encry\\test\\MidSmA_222")),379);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
