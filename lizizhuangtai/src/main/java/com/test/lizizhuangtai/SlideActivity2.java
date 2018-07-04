package com.test.lizizhuangtai;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by ASUS on 2018/5/9.
 */

public class SlideActivity2 extends Activity {

    private SlideView2 all;
    private FrameLayout content;
    private FrameLayout menu;
    private int menuWidth;
    private Button open;
    private Button close;
    private SlideControl slideControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main2);

        all = findViewById(R.id.all);
        content = findViewById(R.id.content);
        menu = findViewById(R.id.menu);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        menuWidth = width*2/3;

        slideControl = new SlideControl(all, menu,content);
        slideControl.doSilde(menuWidth);
        slideControl.setStartLimit(80);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideControl.openMenu();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideControl.closeMenu();
            }
        });
    }


}
