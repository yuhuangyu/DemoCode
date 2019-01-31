package com.test.customizeview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.test.lizizhuangtai.R;

import java.util.Random;

/**
 * Created by fj on 2018/10/22.
 */

public class MActivity extends Activity {

    String[] name={"手机","电脑","玩具","手机","电脑","苹果手机", "笔记本电脑", "电饭煲 ", "腊肉",
            "特产", "剃须刀", "宝宝", "康佳", "特产", "剃须刀", "宝宝", "康佳"};
    private TextView textView;
    private MyFlowLayout flowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_flow);

        flowLayout = findViewById(R.id.mflow);

       /* for (int i = 0; i < name.length; i++) {
            textView = new TextView(this);
            textView.setText(name[i]);
            //设置内边距
//            textView.setPadding(5,5,5,5);
//            textView.setTextSize(20);
            //设置颜色
            textView.setTextColor(Color.RED);
            //添加数据
            myFlowLayout.addView(textView);
        }*/
        // 设置条目的水平间距
        flowLayout.setChildViewHorizontalPadding(DensityUtil.dip2px(this, 15f));        // 设置每行的垂直间距
        flowLayout.setChildViewVerticalPadding(DensityUtil.dip2px(this, 10f));

        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                addTextView("tv");
            } else if (i % 3 == 0) {
                addTextView("textView");
            } else {
                addTextView("this is a textView");
            }
        }


    }

    private void addTextView(final String str) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setText(str);
        textView.setTextSize(20);
        textView.setPadding(DensityUtil.dip2px(this, 5f), DensityUtil.dip2px(this, 10f), DensityUtil.dip2px(this, 5f), DensityUtil.dip2px(this, 5f));
//        textView.setBackgroundColor(Color.BLUE);
        textView.setBackgroundColor(randomColor());
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showSingleInstance(TestActivity.this, str);
//            }
//        });
        flowLayout.addView(textView);
    }


    public int randomColor() {
        Random random = new Random();
        int red = random.nextInt(150);//0-190
        int green = random.nextInt(150);//0-190
        int blue = random.nextInt(150);//0-190
        return Color.rgb(red, green, blue);//使用rgb混合生成一种新的颜色
    }


}
