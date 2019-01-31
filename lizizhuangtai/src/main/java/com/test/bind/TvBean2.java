package com.test.bind;

import android.app.Activity;

import com.test.lizizhuangtai.R;

/**
 * Created by fj on 2018/8/30.
 */

public class TvBean2 extends Bean{
    private int color;

    protected TvBean2(Activity activity) {
        super(activity);
    }

    /*public void setColor(int color) {
        this.color = color;
        observable.notifyObserve(this, "getColor");
    }

    @BindBean(value = {R.id.tv2}, method = "setTextColor", parameter = int.class)
    public int getColor() {
        return color;
    }*/
}
