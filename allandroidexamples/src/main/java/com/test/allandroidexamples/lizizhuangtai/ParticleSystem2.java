package com.test.allandroidexamples.lizizhuangtai;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.SystemClock;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ASUS on 2018/3/22.
 */

public class ParticleSystem2 {

    private static class Particle2 {
        public PointF position;
        public PointF vvv;
        public PointF aaa;
        public float decay = 1;  // 衰减度 0-1
        public int type;         // 颜色选择
    }
    private  Canvas canvas;
    private  View view;
    private CopyOnWriteArrayList<Particle2> _particles = new CopyOnWriteArrayList<Particle2>();
    private final int[] colors = new int[]{Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN, Color.MAGENTA,
            Color.RED, Color.WHITE, Color.DKGRAY, 0xff090909, 0xff098789};
    private Random random = new Random();

    private void addParticle(Particle2 particle2){
        _particles.add(particle2);
    }

    private void removeParticle(Particle2 particle2){
        _particles.remove(particle2);
    }

    private void removeParticle(List<Particle2> particle2){
//        _particles.remove(particle2);
        for (Particle2 particle : particle2)
            _particles.remove(particle);
    }

    public void showParticles(){
        for (int i = 0; i < _particles.size(); i++) {
            showParticle(_particles.get(i));
        }
    }

    private void showParticle(final Particle2 particle2){
        final Paint paint = new Paint()
        {
            {
                this.setColor(colors[particle2.type]);
                setAntiAlias(true);
            }
        };
        paint.setAlpha((int) (255 * particle2.decay));
        canvas.drawCircle(particle2.position.x, particle2.position.y, 15 * particle2.decay, paint);
    }

    private class ThreadParticleLooper{
        private Thread _thread;
        public void start() {
            _thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long lastTime = SystemClock.elapsedRealtime();
                    while (!_thread.isInterrupted()) {
                        onUpdate(SystemClock.elapsedRealtime() - lastTime);
                        if (view != null) view.postInvalidate();
                        lastTime = SystemClock.elapsedRealtime();

                        SystemClock.sleep(20);
                    }
                }
            });
            _thread.start();
        }
        private void onUpdate(long interval) {
            List<Particle2> particles = new ArrayList<Particle2>();

            for (Particle2 particle2 : _particles) {
                if (!update(interval, particle2)) {
//                    particles.add(particle2);
                    removeParticle(particle2);
                }
            }
//            removeParticle(particles);
        }

        private boolean update(long interval, Particle2 particle) {
            float t = interval / 1000f;

            particle.decay=particle.decay - 0.1f * t;

            //v*t + a*t^2/2

            particle.position.offset(offset(particle.vvv.x, particle.aaa.x, t),
                    offset(particle.vvv.y, particle.aaa.y, t));

            particle.vvv.offset(particle.aaa.x * t, particle.aaa.y * t);


            if (particle.decay <= 0.2)
                return false;

            return true;
        }
        private float offset(float v, float a, float t)
        {
            return v * t + a * t * t / 2;
        }
        public void stop() {
            _thread.interrupt();
        }
    }
    public void start(){
        new ThreadParticleLooper().start();
    }

    public void setView(View view){
        this.view = view;
    }
    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }
    public void createParticle2(PointF pos){
        for(int i=0;i<5;i++)
        {
            Particle2 particle2 = new Particle2();
            particle2.position=new PointF(pos.x+(float) (10 * (random.nextDouble() * 2 - 1)),pos.y+(float) (10 * (random.nextDouble() * 2 - 1)));
            particle2.vvv=new PointF((float) (100 * (random.nextDouble() * 2 - 1)), -100);
            particle2.aaa=new PointF(0, 48);
            particle2.type=random.nextInt(10);
            particle2.decay=0.3f + (float) (random.nextDouble() * 0.7f);
            addParticle(particle2);
        }
    }

}
