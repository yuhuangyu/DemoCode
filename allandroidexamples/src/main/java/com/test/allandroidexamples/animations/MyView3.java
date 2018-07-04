package com.test.allandroidexamples.animations;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ASUS on 2018/1/17.
 */
//悬浮球 动画
public class MyView3 extends View {
    Paint paint = new Paint();
    Path path = new Path();
    Path path1 = new Path();
    Point point = new Point(650, 300);
    float animatedValue = 0;
    private int width;
    private int height;

    public MyView3(Context context) {
        super(context);
    }

    public MyView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        path.lineTo(0,0);
        width = this.getWidth();
        height = this.getHeight();

        ValueAnimator valueAnimator = new ValueAnimator();
        float aa = (float) Math.PI*2;
        valueAnimator.setFloatValues(0,aa);
        valueAnimator.setDuration(2000);

        valueAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                double a = animatedValue;
                double b = height/2;
                path.reset();
                path.moveTo(0,300f);
                for (int i = 0; i < width; i++) {
                    path.lineTo(i,(float) (Math.sin((i*Math.PI)/(180)+a)*20+b));
                    invalidate();
                }
                path.lineTo(width,height);
                path.lineTo(0,height);
                invalidate();
            }
        });
        valueAnimator.start();


        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path1.addCircle(width/2,height/2,width/2, Path.Direction.CCW);
        canvas.clipPath(path1);

//        super.onDraw(canvas);

//        canvas.drawRect(0f,0f,300f,300f, paint);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(6f);
        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        path.reset();
//        path.moveTo(300f,300f);
////        path.lineTo(300f,1000f);
//        Float fx = Float.valueOf(point.x);
//        Float fy = Float.valueOf(point.y);
//        path.quadTo(fx,fy,1000f,300f);

//        canvas.drawPath(path,paint);

//        canvas.clipPath()


//        path.reset();

//        double x = 0;
//        double a = 0;
//        double b = 0;
//        int width = getWidth();
//        float y = (float) (Math.sin((animatedValue)/(2*Math.PI)+a)+b);





        canvas.drawPath(path,paint);


    }
}
