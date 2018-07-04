package com.test.markdemo.exercise.webviewtest;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by anye6488 on 2016/9/20.
 */
public class TimeoutHandler
{
    private Timer _timer;
    private Runnable _run;
    private long _timeout;
    private Handler _handler = new Handler(Looper.getMainLooper());

    public TimeoutHandler(final Runnable runnable, long timeout)
    {
        _run = runnable;
        _timeout = timeout;
    }

    public synchronized void start()
    {
        _timer = new Timer();

        _timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (_run != null)
                {
                    _handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            _run.run();
                        }
                    });
                }
            }
        }, _timeout);
    }

    public void reset()
    {
        _timer.cancel();

        start();
    }

    public synchronized void cancel()
    {
        _timer.cancel();
        _timer = null;
    }
}
