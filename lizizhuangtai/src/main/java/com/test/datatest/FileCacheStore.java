package com.test.datatest;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.test.data.Bamboo;

import java.io.File;

/**
 * Created by ASUS on 2018/4/11.
 */

@SuppressWarnings("ALL")
public class FileCacheStore {
    private static FileCacheStore instance;
    private Bamboo bamboo;
    public static FileCacheStore get(Context context) {
        if (instance == null) {
            synchronized (FileCacheStore.class) {
                if (instance == null) {
//                    File dir = IOUtils.mkdir(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + Digest.md5(context.getPackageName() + Build.MANUFACTURER + Config.current(context).getAppID()).substring(0, 8) + "/");
//                    String filesDir = context.getCacheDir().getAbsolutePath();
                    String filesDir = Environment.getExternalStorageDirectory().getPath() + "/test";

                    String libPath = filesDir + File.separator + "appflyer";
                    File dir = new File(libPath);
                    instance = new FileCacheStore(dir);
                }
            }
        }
        return instance;
    }
    private FileCacheStore(File dir) {
        try {
//            File file = new File(dir, Digest.md5(dir.getAbsolutePath()).substring(0, 8));
//            if (file.exists() && file.length() >= 10 * 1024 * 1024) {
//                file.delete();
//            }
            bamboo = new Bamboo(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void put(String key, String value) {
        try {
            bamboo.getBambooServer().write(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putSync(String key, String value, int timeOut) {
        throw new RuntimeException("can't invoke timeout putSync method");
    }

    public final void put(String key, String e, int timeOut) {
        throw new RuntimeException("can't invoke timeout put method");
    }
    public String get(String key, String def) {
        try {
            String v = bamboo.getBambooServer().read(key);
            if (TextUtils.isEmpty(v))
                return def;
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }
    public void remove(String key) {
        try {
            bamboo.getBambooServer().cut(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
