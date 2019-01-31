package com.test.pushlive;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


@SuppressLint("NewApi")
public class LiveJobService extends JobService {
    private static final int KEEP_ALIVE_INTERVAL = 1 * 60 * 1000;
    public static void StartJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context
                .JOB_SCHEDULER_SERVICE);
//        setPersisted 在设备重启依然执行
        JobInfo.Builder builder = new JobInfo.Builder(10,
                new ComponentName(context.getPackageName(), LiveJobService.class.getName()))
                .setPersisted(true);
        //小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder.setPeriodic(KEEP_ALIVE_INTERVAL);
        } else {
            //延迟执行任务
            builder.setMinimumLatency(KEEP_ALIVE_INTERVAL);
        }

        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("sdk","start job");
        start();
        //如果7.0以上 轮训
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StartJob(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        start();
        return false;
    }

    private void start(){
        Intent service = new Intent(this,LiveService.class);
        this.startService(service);
    }
}
