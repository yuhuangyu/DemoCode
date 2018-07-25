package com.api.utils.gpdownload;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.MarketSession;

import java.io.File;
import java.io.InputStream;
/*
*
* 谷歌下载jar包中
* device-honami.properties 已删除，放入resources文件中
*
* properties.load(Main.class.getClassLoader().getSystemResourceAsStream("device-honami.properties"));
* 修改为
* properties.load(Main.class.getClassLoader().getResourceAsStream("device-honami.properties"));
* */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        new Thread(new Runnable(){
            @Override
            public void run() {
//                InputStream systemResourceAsStream = getClassLoader().getResourceAsStream("device-honami.properties");
//                String pkg = "avidly.fishing.free.buyu";
                String pkg = "com.game.ws.cutfruit";
//                String pkg = "com.bsbportal.music";
                MarketSession session = new MarketSession();
                session.setAndroidId("3551e4809aa4235d");
                try {
                    session.login("xiayutianlezenmeban@gmail.com", "ll123456");
//                    session.login("kuaiweijian@gmail.com", "kuai@520");
//                    session.login("kuaijianjian@gmail.com", "xiaoshuo001");

                    int versionCode = session.getVersionCode(pkg);
                    System.out.println("versionCode "+versionCode);
                    Log.e("Sdk", "==versionCode "+versionCode);
                    String path = Environment.getExternalStorageDirectory().getPath() + "/test/";
                    File file = new File(path, pkg + ".apk");
                    session.startDownload(pkg,  file);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
