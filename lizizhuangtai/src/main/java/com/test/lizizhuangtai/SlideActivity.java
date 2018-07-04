package com.test.lizizhuangtai;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

/**
 * Created by ASUS on 2018/5/9.
 */

public class SlideActivity  extends Activity {

    private SlideView slideview;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        slideview = findViewById(R.id.slideview);


        int limitMinX = 0;
        int limitMaxX = getWindowManager().getDefaultDisplay().getWidth()/4*3;
        int limitMinY = 0;
        int limitMaxY = getWindowManager().getDefaultDisplay().getHeight()/4*3;
        slideview.setlimit(limitMinX,limitMaxX,limitMinY,limitMaxY);

        slideview.onSlide(new SlideView.ISlide() {

            @Override
            public void onDragBegin(int x, int y) {
                Log.i("sdk", "onDragBegin  "+x+"  "+y);
            }

            @Override
            public void onDrag(int x, int y) {
                slideview.layout(x,y,x+slideview.getWidth(),y+slideview.getHeight());
            }

            @Override
            public void onDragEnd(int x, int y) {
                slideview.layout(0,0,slideview.getWidth(),slideview.getHeight());
            }
        });

    }


}
