package com.test.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.lizizhuangtai.R;

/**
 * Created by fj on 2018/8/28.
 */

public class Main extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

//        CalView viewById = (CalView)findViewById(R.id.calendar);

//        viewById.invalidate();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        //获取今天是多少号
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        /*Date cM = str2Date(getMonthStr(new Date()));
        //判断是否为当月
        if(cM.getTime() == month.getTime()){
            isCurrentMonth = true;
            selectDay = currentDay;//当月默认选中当前日
        }else{
            isCurrentMonth = false;
            selectDay = 0;
        }
        Log.d(TAG, "设置月份："+month+"   今天"+currentDay+"号, 是否为当前月："+isCurrentMonth);*/
//        calendar.setTime(month);
//        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        //第一行1号显示在什么位置（星期几）
//        int firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        /*lineNum = 1;
        //第一行能展示的天数
        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int shengyu = dayOfMonth - firstLineNum;
        while (shengyu>7){
            lineNum ++;
            shengyu-=7;
        }
        if(shengyu>0){
            lineNum ++;
            lastLineNum = shengyu;
        }
        Log.i(TAG, getMonthStr(month)+"一共有"+dayOfMonth+"天,第一天的索引是："+firstIndex+"   有"+lineNum+
                "行，第一行"+firstLineNum+"个，最后一行"+lastLineNum+"个");
*/
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        calendar.
    }
}
