package com.test.allandroidexamples.ParticleSystem;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;
/*
*
* 触摸屏幕，粒子散开动画
* 粒子触发器
*
* */
public class TestActivity extends Activity {

    private Random random = new Random();;
    private View _view;
    private ParticleSystem _system;
    private ParticleSystem2 particleSystem2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_main);
//        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 0, 0, 0));
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        this.getWindow().setBackgroundDrawable(colorDrawable);


//        doParticle();

        mytest();

    }

    private void mytest() {
        particleSystem2 = new ParticleSystem2();
        _view = new View(this){
            @Override
            protected void onDraw(Canvas canvas) {
                particleSystem2.setCanvas(canvas);
                particleSystem2.showParticles();
            }
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    particleSystem2.createParticle2(new PointF(event.getX(),event.getY()));
                }
                return super.onTouchEvent(event);
            }
        };
        particleSystem2.setView(_view);
        particleSystem2.start();
        setContentView(_view);
    }

    private void doParticle() {



        ParticleSystem.ParticleGenerator lgenerator =new ParticleSystem.ParticleGenerator()
        {
            @Override
            public void generate(PointF pos)
            {
                for(int i=0;i<5;i++)
                {
                    ParticleSystem.Particle particle = new ParticleSystem.Particle();
                    particle.position(new PointF(pos.x+(float) (10 * (random.nextDouble() * 2 - 1)),pos.y+(float) (10 * (random.nextDouble() * 2 - 1))));
                    particle.speed(new PointF((float) (100 * (random.nextDouble() * 2 - 1)), -100));
                    particle.acceleration(new PointF(0, 48));
                    particle.type(random.nextInt(10));
                    particle.decay(0.3f + (float) (random.nextDouble() * 0.7f));

                    system().add(particle);
                }
            }
        };

        ParticleSystem.ParticleGenerator generator = new ParticleSystem.ParticleGenerator()
        {
            @Override
            public void generate(PointF pos)
            {
                for(int i=0;i<5;i++)
                {
                    ParticleSystem.Particle particle = new ParticleSystem.Particle();
                    particle.position(new PointF(pos.x+(float) (10 * (random.nextDouble() * 2 - 1)),pos.y+(float) (10 * (random.nextDouble() * 2 - 1))));
                    particle.speed(new PointF((float) (100 * (random.nextDouble() * 2 - 1)), (float) (100 * (random.nextDouble() * 2 - 1))));
                    particle.acceleration(new PointF((float) (50 * (random.nextDouble() * 2 - 1)), (float) (50 * (random.nextDouble() * 2 - 1))));
                    particle.type(random.nextInt(10));
                    particle.decay(0.3f + (float) (random.nextDouble() * 0.7f));

                    system().add(particle);
                }
            }
        };

        _system = new ParticleSystem(lgenerator, new ParticleSystem.RenderBuilder()
        {
            @Override
            public ParticleSystem.ParticleRender getRender(final ParticleSystem.Particle particle)
            {

                final int[] colors = new int[]{Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN, Color.MAGENTA,
                        Color.RED, Color.WHITE, Color.DKGRAY, 0xff090909, 0xff098789};
                final Paint paint = new Paint()
                {
                    {
                        this.setColor(colors[particle.type()]);
                        setAntiAlias(true);
                    }
                };

                ParticleSystem.ParticleRender render = new ParticleSystem.ParticleRender()
                {
                    @Override
                    public void draw(Canvas canvas, ParticleSystem.Particle particle)
                    {
                        paint.setAlpha((int) (255 * particle.decay()));
                        canvas.drawCircle(particle.position().x, particle.position().y, 15 * particle.decay(), paint);
                    }
                };
                render.particle(particle);
                return render;
            }
        }, new ParticleSystem.ParticleUpdater()
        {
            @Override
            public boolean update(long interval, ParticleSystem.Particle particle)
            {
                float t = interval / 1000f;

                particle.decay(particle.decay() - 0.1f * t);

                //v*t + a*t^2/2

                particle.position().offset(offset(particle.speed().x, particle.acceleration().x, t),
                        offset(particle.speed().y, particle.acceleration().y, t));

                particle.speed().offset(particle.acceleration().x * t, particle.acceleration().y * t);

//                if(particle.position().x <= 0 || particle.position().x >= getResources().getDisplayMetrics().widthPixels)
//                {
//                    particle.speed().x = -particle.speed().x;
//                    particle.acceleration().x = -particle.acceleration().x;
//                }
//
//                if(particle.position().y <= 0)
//                {
//                    particle.speed().y = -particle.speed().y;
//                    particle.acceleration().y = -particle.acceleration().y;
//                }

                if (particle.decay() <= 0.2)
                    return false;

                return true;
            }

            @Override
            public void onCompleted()
            {
                if (_view != null)
                    _view.postInvalidate();
            }

            private float offset(float v, float a, float t)
            {
                return v * t + a * t * t / 2;
            }
        });

        _system.start();

        final Drawable drawable = _system.render();
        _view = new View(this)
        {
            @Override
            protected void onDraw(Canvas canvas)
            {
                drawable.draw(canvas);
            }
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    _system.generate(new PointF(event.getX(),event.getY()));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    return true;
                }
                return super.onTouchEvent(event);
            }
        };
        setContentView(_view);
    }


}
