package com.test.live;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.lizizhuangtai.R;
import com.test.pushlive.MainService;

import java.io.BufferedInputStream;
import java.io.DataInputStream;


/**
 * Created by fj on 2018/8/6.
 */

public class MainActivity extends Activity {
    private int mFrequence = 8000;
    private long INTERVALMILLIS = 1000 * 60 * 60 * 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new PlayTask().execute();

    }



    class PlayTask extends AsyncTask<Void, Void, Void> {

        private boolean mIsPlaying;
        private int mFrequence = 8000;

        @Override
        protected Void doInBackground(Void... arg0) {
            mIsPlaying = true;
            for (; mIsPlaying; ) {
                int bufferSize = AudioTrack.getMinBufferSize(mFrequence, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
                short[] buffer = new short[bufferSize];
                try {
                    // 定义输入流，将音频写入到AudioTrack类中，实现播放
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(getResources().openRawResource(R.raw.a)));
                    // 实例AudioTrack
                    AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                            mFrequence,
                            AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            bufferSize,
                            AudioTrack.MODE_STREAM);
                    track.setStereoVolume(0, 0);
                    // 开始播放
                    track.play();
                    // 由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                    while (mIsPlaying && dis.available() > 0) {
                        int i = 0;
                        while (dis.available() > 0 && i < buffer.length) {
                            buffer[i] = dis.readShort();
                            i++;
                        }
                        // 然后将数据写入到AudioTrack中
                        track.write(buffer, 0, buffer.length);
                    }
                    // 播放结束
                    track.stop();
                    dis.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("slack", "error:" + e.getMessage());
                }
            }
            return null;
        }
        protected void onPostExecute(Void result) {}
        protected void onPreExecute() {}
    }

    private void runService() {
        Intent intent = new Intent(this, MainService.class);
        PendingIntent mAlarmPIent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // long firstTime = System.currentTimeMillis();
        // firstTime表示第1次运行时要等待的时间，也就是执行延迟时间，单位是毫秒。
        long firstTime = SystemClock.elapsedRealtime() + INTERVALMILLIS;
        AlarmManager mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (mAlarmManager == null) {
            return;
        }
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, firstTime, INTERVALMILLIS, mAlarmPIent);
    }
}

