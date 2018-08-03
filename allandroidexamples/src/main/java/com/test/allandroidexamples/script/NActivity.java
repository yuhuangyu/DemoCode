package com.test.allandroidexamples.script;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

//import com.omg.ver.Config;


/**
 * Created by fj on 2018/7/31.
 *
 * 在build.gradle文件中 创建一个 /build/generated/ver/com/omg/ver/Config的文件（编译时创建）
 *
 * 在工程的java文件中可拿到定义的对象
 *
 * 先编译 copyKing ，才可用，不然编译报错
 */

public class NActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String vrMachineName = Config.VrMachineName;
//        Log.e("sdk", "vrMachineName   "+vrMachineName);
    }

}
