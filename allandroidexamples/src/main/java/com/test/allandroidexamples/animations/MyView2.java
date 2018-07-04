package com.test.allandroidexamples.animations;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ASUS on 2018/1/17.
 */
//正在加载类型画圈 动画
public class MyView2 extends View {
    Paint paint = new Paint();
    Path path = new Path();
    Point point = new Point(650, 300);
    RectF rectF = new RectF(400,400,600,600);
    int animatedValue = 0;
    int animatedValue2 = 0;
    public MyView2(Context context) {
        super(context);
    }

    public MyView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0,180,0);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();

        ValueAnimator valueAnimator2 = new ValueAnimator();
        valueAnimator2.setIntValues(0,360,720);
        valueAnimator2.setDuration(2000);
//        valueAnimator2.setInterpolator(new Interpolator() {
//            @Override
//            public float getInterpolation(float input) {
//                return input;
//            }
//        });
        valueAnimator2.setRepeatCount(-1);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue2 = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator2.start();

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

//        canvas.drawRect(0f,0f,300f,300f, paint);
        paint.setColor(Color.BLACK);
//        paint.set
        paint.setStrokeWidth(24f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        int r = 100;
        int rdis = 4;
        float sx = (float) ((r)*Math.cos(animatedValue2*Math.PI/180)+500);
        float sy = (float) ((r)*Math.sin(animatedValue2*Math.PI/180)+500);

        float ex = (float) ((r)*Math.cos((animatedValue2+animatedValue)*Math.PI/180)+500);
        float ey = (float) ((r)*Math.sin((animatedValue2+animatedValue)*Math.PI/180)+500);

        canvas.drawArc(rectF,animatedValue2,animatedValue,false,paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.RED);
        canvas.drawCircle(sx,sy,12,paint);
        canvas.drawCircle(ex,ey,12,paint);
    }
}
