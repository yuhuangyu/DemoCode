package com.test.bind;

import android.app.Activity;

import com.test.lizizhuangtai.R;

/**
 * Created by fj on 2018/8/30.
 */

public class TvBean extends Bean{
    private String name;
    private int color;

    protected TvBean(Activity activity) {
        super(activity);
    }

    public void setName(String name){
        this.name = name;
        observable.notifyObserve(this, "getName");
    }

    /*@BindBean(value = {R.id.tv,R.id.tv2}, method = "setText", parameter = CharSequence.class)
    public String getName(){
        return name;
    }

    public void setColor(int color) {
        this.color = color;
        observable.notifyObserve(this, "getColor");
    }

    @BindBean(value = {R.id.tv}, method = "setTextColor", parameter = int.class)
    public int getColor() {
        return color;
    }*/
}
