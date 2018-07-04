package com.test.lizizhuangtai;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by ASUS on 2018/5/9.
 */

public class SlideView extends RelativeLayout {
    private Paint paint = new Paint();
    private ISlide iSlide = null;
    private int lastX;
    private int lastY;
    private int downX;
    private int downY;
    int width = this.getWidth();
    int height = this.getHeight();

    private int limitMinX;
    private int limitMaxX;
    private int limitMinY;
    private int limitMaxY;

    public SlideView(Context context) {
        super(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (iSlide != null) {
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                iSlide.onDragBegin(x,y);
            }
        }else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (iSlide != null) {
                if (isLimit(x-downX,y-downY)){
                    iSlide.onDrag(x-downX,y-downY);
                }
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (iSlide != null) {
                iSlide.onDragEnd(x-downX,y-downY);
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (iSlide != null) {
                iSlide.onDragEnd(x-downX,y-downY);
            }
        }
        lastX = (int) event.getRawX();
        lastY = (int) event.getRawY();
        return true;
    }

    private boolean isLimit(int x, int y) {

        if (x > limitMinX && x<limitMaxX-width && y > limitMinY && y<limitMaxY-height) {
            return true;
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getWidth();
        height = this.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    public void onSlide(ISlide iSlide){
        this.iSlide = iSlide;
    }

    public void setlimit(int limitMinX, int limitMaxX,int limitMinY, int limitMaxY) {
        this.limitMinX = limitMinX;
        this.limitMaxX = limitMaxX;
        this.limitMinY = limitMinY;
        this.limitMaxY = limitMaxY;
    }

    public interface ISlide {
        void onDragBegin(int x, int y);
        void onDrag(int x, int y);
        void onDragEnd(int x, int y);
    }
}
