package com.test.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fj on 2018/9/12.
 */

public class CalView extends ViewGroup {

    private Context context1;

    public CalView(Context context) {
        super(context);
        context1 = context;
    }

    public CalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        context1 = context;
    }

    public CalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context1 = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(context1);
            textView.setText("i "+i);
            addView(textView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //获取当前ViewGroup的宽度
        int width = getWidth();
        int height = getHeight();
        int left = 0;
        int top = 0;
        int childCount = getChildCount();
        int lineNum = 0;
        int allLine = childCount/7;
        for (int i = 0; i < childCount; i++) {
            lineNum = i % 7;
            View child = getChildAt(i);
            int cLeft = left;
            int cTop = top;
            int cRight = cLeft + width/7;
            int cBottom = cTop + height/allLine;

            child.layout(cLeft,cTop,cRight,cBottom);
            if (cRight + width/7 >= width) {
                left = 0;
            }else {
                left = cRight;
            }
            top = top + lineNum * height / allLine;
        }
    }

//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        // TODO Auto-generated method stub
//
//        return new MarginLayoutParams(getContext(), attrs);
//
//    }
//
//    @Override
//    public void invalidate() {
//        requestLayout();
//        super.invalidate();
//    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }
}
