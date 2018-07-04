package com.test.allandroidexamples.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by ASUS on 2018/5/17.
 */

public class SlideView2  extends RelativeLayout {

    private int lastX;
    private int lastY;
    private int downX;
    private int downY;
    private int menuWidth;
    private boolean isOpen = false;
    private int length = 0;
    private long lastTime;
    private ISlide iSlide = null;

    public SlideView2(Context context) {
        super(context);
    }

    public SlideView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (length > 0 && x >= length && !isOpen) {
                return false;
            }
            if (iSlide != null) {
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                iSlide.onDragBegin(x,y);
            }
        }else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (iSlide != null) {
                iSlide.onDrag(x,y,lastX,lastY,lastTime);
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (iSlide != null) {
                iSlide.onDragEnd(x,y,lastX,lastY,lastTime);
            }
        }
        lastX = (int) event.getRawX();
        lastY = (int) event.getRawY();
        lastTime = System.currentTimeMillis();
        return true;
    }

    public interface ISlide {
        void onDragBegin(int x, int y);
        void onDrag(int x, int y, int lx, int ly, long time);
        void onDragEnd(int x, int y, int lx, int ly, long time);
    }

    public void onSlide(ISlide iSlide){
        this.iSlide = iSlide;
    }
    public void setIsOpen(boolean isOpen){
        this.isOpen = isOpen;
    }
    public void setStartLimit(int length){
        this.length = length;
    }
}
