package com.test.download;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.lizizhuangtai.R;

import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by fj on 2018/8/9.
 */

public class test extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        new Thread(new Runnable() {
            @Override
            public void run() {

                DownLoadClientBase base = DownLoadClientBase.create();
//                File file = new File(test.this.getDir("apps", Context.MODE_PRIVATE), "aaa");
                String path = Environment.getExternalStorageDirectory().getPath() + "/test/aaa";

                base.download("http://appjoyreach.cn:7898/group1/M00/00/06/wKgBJ1wlzlWABCQFABG5uPiz-eU048.apk", path, new DownLoadClientBase.DownloadListener()
                {
                    @Override
                    public void onCompleted(DownLoadClientBase.DownloadTask task)
                    {
                        System.out.println("suc");
                        Log.e("sdk", "=== suc");
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        e.printStackTrace();
                        Log.e("sdk", "=== onError "+e.getMessage());
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onProgress(long current, long total)
                    {
                        Log.e("sdk", "total:" + total + " current:" + current);
                    }
                });
            }
        }).start();

    }
}
