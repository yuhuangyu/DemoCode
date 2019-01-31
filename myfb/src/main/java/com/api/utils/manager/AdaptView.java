package com.api.utils.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * Created by fj on 2018/10/31.
 */

public class AdaptView extends AdapterView {

    private Adapter adapter;
    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public AdaptView(Context context) {
        super(context);
    }

    public AdaptView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }


//    @Override
//    public void setAdapter(ListAdapter adapter) {
//        this.adapter = adapter;
//    }


    @Override
    public View getSelectedView() {


        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
//        View view = getAdapter().getView(0, null, this);
//        getAdapter().
        int width = getWidth();
        int height = getHeight();
        Log.e("sdk","== "+width+",height"+height+"--"+getAdapter().getCount());

        if (height != 0) {
            int nowW = 0;
            int nowH = 0;
            int line = 0;
            int lineMax = 0;
            int num = 0;
            for (int i = 0; i < getAdapter().getCount(); i++) {
//            View childAt = getChildAt(i);
                View childAt = getAdapter().getView(i, null, this);
                childAt.measure(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.AT_MOST));
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();

                Log.e("sdk","== "+measuredWidth+",measuredHeight"+measuredHeight);
                if (nowW+measuredWidth <= width) {
                    this.addViewInLayout(childAt,i,new LayoutParams(measuredWidth,measuredHeight));
                    childAt.layout(nowW,nowH,nowW+measuredWidth,nowH+measuredHeight);
                    nowW += measuredWidth;
                }else {
                    line++;
                    nowW = 0;
                    nowH += measuredHeight;
                    this.addViewInLayout(childAt,i,new LayoutParams(measuredWidth,measuredHeight));
                    childAt.layout(nowW,nowH,nowW+measuredWidth,nowH+measuredHeight);
                    nowW += measuredWidth;
                }
//            if (nowH + measuredHeight > height) {
//                lineMax = line + 1;
//            }
                num++;

//            if (line > lineMax) {
//                break;
//            }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        /*int nowW = 0;
        int nowH = 0;
        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getAdapter().getView(i, null, this);
            childAt.measure(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.AT_MOST));
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();


            if (nowW+measuredWidth <= width) {
                childAt.layout(nowW,nowH,nowW+measuredWidth,nowH+measuredWidth);
                nowW += measuredWidth;

            }else {
                nowW = 0;
                nowH += measuredHeight;

                childAt.layout(nowW,nowH,nowW+measuredWidth,nowH+measuredWidth);
            }

        }*/
    }

    @Override
    public void setSelection(int position) {

    }
}
