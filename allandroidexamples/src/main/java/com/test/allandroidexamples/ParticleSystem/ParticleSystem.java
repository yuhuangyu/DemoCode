package com.test.allandroidexamples.ParticleSystem;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by anye on 2018/3/21.
 */

public class ParticleSystem {
    public interface ParticleModule {
        void attach(ParticleSystem system);
    }

    public static abstract class ParticleModuleBase {
        private ParticleSystem _system;

        public void attach(ParticleSystem system) {
            _system = system;
        }

        public ParticleSystem system() {
            return _system;
        }
    }

    public static abstract class ParticleGenerator extends ParticleModuleBase {
        public abstract void generate(PointF pos);
    }

    public static abstract class ParticleUpdater {
        public abstract boolean update(long interval, Particle particle);

        public abstract void onCompleted();
    }

    public abstract class ParticleLooper extends ParticleModuleBase {
        public abstract void start();

        public abstract void stop();
    }

    public class ThreadParticleLooper extends ParticleLooper {
        private Thread _thread;

        private void onUpdate(long interval) {
            ParticleUpdater updater = updater();

            List<Particle> particles = new ArrayList<Particle>();

            for (Particle particle : particles()) {
                if (!updater.update(interval, particle)) {
                    particles.add(particle);
                }
            }

            system().remove(particles);

            updater.onCompleted();
        }

        @Override
        public void start() {
            _thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long lastTime = SystemClock.elapsedRealtime();
                    while (!_thread.isInterrupted()) {
                        onUpdate(SystemClock.elapsedRealtime() - lastTime);
                        lastTime = SystemClock.elapsedRealtime();

                        SystemClock.sleep(20);
                    }
                }
            });
            _thread.start();
        }

        @Override
        public void stop() {
            _thread.interrupt();
        }
    }

    public static class Particle {
        private PointF position;
        private PointF speed;
        private PointF acceleration;

        private float decay = 1;
        private int type;

        public PointF position() {
            return position;
        }

        public void position(PointF position) {
            this.position = position;
        }

        public PointF speed() {
            return speed;
        }

        public void speed(PointF speed) {
            this.speed = speed;
        }

        public PointF acceleration() {
            return acceleration;
        }

        public void acceleration(PointF acceleration) {
            this.acceleration = acceleration;
        }

        public float decay() {
            return decay;
        }

        public void decay(float decay) {
            this.decay = decay;
        }

        public int type() {
            return type;
        }

        public void type(int type) {
            this.type = type;
        }
    }


    public class SystemRender extends Drawable implements ParticleModule {
        private ParticleSystem _system;
        private RenderBuilder _builder;

        @Override
        public void draw(@NonNull Canvas canvas) {
            for (Particle particle : _system.particles()) {
                draw(canvas, _builder.getRender(particle));
            }
        }

        private void draw(Canvas canvas, ParticleRender particle) {
            particle.draw(canvas);
        }

        @Override
        public void setAlpha(int i) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }

        @Override
        public void attach(ParticleSystem system) {
            _system = system;
            _builder = system.builder();
        }
    }

    public static abstract class ParticleRender extends Drawable {

        private Particle _particle;

        public void particle(Particle _particle) {
            this._particle = _particle;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (_particle != null) {
                draw(canvas, _particle);
            }
        }

        public abstract void draw(Canvas canvas, Particle particle);


        @Override
        public void setAlpha(int i) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    public interface RenderBuilder {
        ParticleRender getRender(Particle particle);
    }

    private List<Particle> _particles = new LinkedList<Particle>();
    private ParticleLooper _looper;
    private ParticleUpdater _updater;
    private ParticleGenerator _generator;
    private RenderBuilder _builder;

    public ParticleSystem(ParticleGenerator generator, RenderBuilder builder, ParticleUpdater updater) {
        _generator = generator;
        _generator.attach(this);

        _updater = updater;
        _builder = builder;
    }

    public synchronized final List<Particle> particles() {
        List<Particle> list = new LinkedList<Particle>();
        for (Particle particle : _particles)
            if (particle.decay() > 0) {
                list.add(particle);
            }

        return list;
    }

    public synchronized final void add(Particle particle) {
        _particles.add(particle);
    }

    public synchronized final void remove(Particle particle) {
        _particles.remove(particle);
    }

    public synchronized final void remove(Iterable<Particle> list) {
        for (Particle particle : list)
            _particles.remove(particle);
    }

    public ParticleUpdater updater() {
        return _updater;
    }

    public RenderBuilder builder() {
        return _builder;
    }

    public void updater(ParticleUpdater updater) {
        _updater = updater;
    }

    public ParticleLooper looper() {
        if (_looper == null) {
            _looper = new ThreadParticleLooper();
            _looper.attach(this);
        }

        return _looper;
    }


    public void generate(PointF pos) {
        _generator.generate(pos);
    }

    public Drawable render() {
        SystemRender render = new SystemRender();
        render.attach(this);
        return render;
    }

    public void start() {
        looper().start();
    }

    public void stop() {
        looper().stop();

        _looper = null;
    }
}
