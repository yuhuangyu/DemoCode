package com.test.allandroidexamples.animations;

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
// 有弹性的线 动画
public class MyView extends View {
    Paint paint = new Paint();
    Path path = new Path();
    Point point = new Point(650, 300);
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            point = new Point(650, 300);
            invalidate();
            return true;
        }else if (event != null) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            point = new Point(x, y);
            invalidate();

            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

//        canvas.drawRect(0f,0f,300f,300f, paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        path.moveTo(300f,300f);
//        path.lineTo(300f,1000f);
        Float fx = Float.valueOf(point.x);
        Float fy = Float.valueOf(point.y);
        path.quadTo(fx,fy,1000f,300f);

        canvas.drawPath(path,paint);
    }
}
