package com.test.lizizhuangtai;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity2 extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vvc.fff.ggd.a.a(true);
        vvc.fff.ggd.a.b(true);
        if (hh.bfg.zq.core.getInstance().isRunning() == false) {
            hh.bfg.zq.core.getInstance().init(this, "0500001");
        }
//        Object k = null;
//        getWindow().addFlags( WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//        k.wait
    }
    public static boolean test(Context context){
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
