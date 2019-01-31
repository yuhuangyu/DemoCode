package com.test.pushlive;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * Created by fj on 2018/8/6.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("sdk", "== onStartJob ");
        startMainService();
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("sdk", "== onStopJob ");
        startMainService();
        return false;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("sdk", "== onTaskRemoved ");
        startMainService();
    }

    public void startMainService(){
        startService(MainService.getIntentAlarm(this));
    }
}
