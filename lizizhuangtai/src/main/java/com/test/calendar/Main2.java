package com.test.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.lizizhuangtai.R;

import java.util.Date;

/**
 * Created by fj on 2018/8/28.
 */

public class Main2 extends Activity implements View.OnClickListener, View.OnTouchListener {
    private CalendarViewTest cal;
    private TextView tv_y;
    private TextView tv_m;
    private int touchX;
    private int touchY;
    private int width;
    private float touchLastX;
    private float touchLastY;
    private int touchSlop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        cal = (CalendarViewTest)findViewById(R.id.cal);
        Button bt_1 = (Button)findViewById(R.id.bt_1);
        Button bt_2 = (Button)findViewById(R.id.bt_2);
        tv_y = (TextView)findViewById(R.id.tv_ym1);
        tv_m = (TextView)findViewById(R.id.tv_ym3);

        tv_y.setText(DateUtil.getYear()+"");
        tv_m.setText(DateUtil.getMonth()+"");

        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        width = getWindowManager().getDefaultDisplay().getWidth();
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        cal.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_1) {
            long lower = cal.lower();
            setYM(lower);
        }else if (v.getId() == R.id.bt_2) {
            long add = cal.add();
            setYM(add);
        }
    }

    private void setYM(long lower) {
        int i = (int) (lower / 12);
        int i1 = (int) (lower % 12);
        if (i1 == 0) {
            i1 = 12;
        }
        Log.e("sdk", "i "+i+" ,i1 "+i1);
        tv_y.setText(i+"");
        tv_m.setText(i1+"");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录点击的坐标
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                touchLastX = event.getX();
                touchLastY = event.getY();
                if (Math.abs(touchLastX - touchX) < touchSlop
                        && Math.abs(touchLastY - touchY) < touchSlop) {// 判断是否符合正常点击
                    // 计算出所点击的数组序列
                    int dateNumX = (int) (touchLastX / cal.dateNumWidth);
                    int dateNumY = (int) (touchLastY / cal.dateNumWidth);
                    // 使用回调函数响应点击日历日期
                    int i = cal.dateNum[dateNumY][dateNumX];
                    CalendarViewTest.CalendarState calendarState = cal.calendarStates[dateNumY][dateNumX];

                    Log.e("sdk", "i "+i+" ,calendarState "+calendarState.toString());
                }
                if (touchLastX -touchX > width/3) {
                    long lower = cal.lower();
                    setYM(lower);
                }
                if (touchX- touchLastX > width/3) {
                    long add = cal.add();
                    setYM(add);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                touchLastX = event.getX();
                touchLastY = event.getY();
                break;
            default:
                break;
        }
        return true;
    }
}
