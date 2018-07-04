package com.test.lizizhuangtai;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ASUS on 2018/3/21.
 */

public class Lizi extends View {
    public Lizi(Context context) {
        super(context);
    }

    public Lizi(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Lizi(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();

            int a = randInt(1,10);
            int v = randInt(-10,10);
            int showTime = randInt(1,10);
            int raduis = randInt(1,10);
            Color color = null;

            LiziPoint liziPoint = new LiziPoint(v,a,showTime,raduis,color,x,y);
            liziPointList.addLiziPoint(liziPoint);
        }
        return super.onTouchEvent(event);
    }
    LiziPointList liziPointList = new LiziPointList();

    public void test(float x, float y){

        while (true){
            showLiziPoint(liziPointList);
            SystemClock.sleep(200);
            liziPointList.updata(200);
        }


    }

    private void showLiziPoint(LiziPointList liziPointList) {
        for (int i = 0; i < liziPointList.liziPoints.size(); i++) {

        }
    }

    class LiziPointList{
        List<LiziPoint> liziPoints = null;
        public LiziPointList(){
            liziPoints = new ArrayList<>();
        }
        private void addLiziPoint(LiziPoint liziPoint){
            liziPoints.add(liziPoint);
        }
        private void removeLiziPoint(LiziPoint liziPoint){
            liziPoints.remove(liziPoint);
        }
        private void updata(int t){
            List<LiziPoint> liziPoints = new ArrayList<>();
            for (int i = 0; i < liziPoints.size(); i++) {
                LiziPoint liziPoint = liziPoints.get(i);

                if (liziPoint.showTime-t > 0) {
                    int a = liziPoint.a;
                    int v = liziPoint.v;
                    float x = liziPoint.x;
                    float y = liziPoint.y;
                    if (v > 0) {
                        v = v + a*t;
                    }else {
                        v = v - a*t;
                    }
                    float xx = x + v*t+a*t*t/2;
                    float yy = y + v*t+a*t*t/2;
//                    point = new Point(xx,yy);
                    LiziPoint liziPointNow = new LiziPoint(v,a,liziPoint.showTime-t,liziPoint.raduis,liziPoint.color,xx,yy);
                    liziPoints.add(liziPointNow);
                }
            }
        }


    }

    class LiziPoint{
        float x = 0;
        float y = 0;
        int a = 0;
        int v = 0;
        int showTime = 0;
        int raduis = 0;
        Color color = null;
//        Point point = new Point(0,0);
        public LiziPoint(int v, int a, int showTime, int raduis, Color color, float x, float y){
            this.a = a;
            this.v = v;
            this.showTime = showTime;
            this.raduis = raduis;
            this.color = color;
            this.x = x;
            this.y = y;
        }

        /*private Point nowPoint(Point point, int t){
            if (v > 0) {
                v = v + a*t;
            }else {
                v = v - a*t;
            }

            int xx = point.x + v*t+a*t*t/2;
            int yy = point.y + v*t+a*t*t/2;

            return new Point(xx,yy);
        }*/

    }

    public static int randInt(int min, int max)
    {
        Random _rand = new Random(System.nanoTime());
        return _rand.nextInt(max - min + 1) + min;
    }
}
