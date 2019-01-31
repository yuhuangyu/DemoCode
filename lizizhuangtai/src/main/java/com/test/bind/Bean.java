package com.test.bind;

import android.app.Activity;

/**
 * Created by fj on 2018/8/30.
 */

public class Bean {
    protected Observable observable;
    protected Bean(Activity activity){
        this.observable = new Observable(activity);
    }
}
