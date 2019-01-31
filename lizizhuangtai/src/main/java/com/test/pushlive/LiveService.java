package com.test.pushlive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


public class LiveService extends Service
{
    private final int KEEP_ALIVE_INTERVAL = 1 * 60 * 1000;
    private final long SYNC_FREQUENCY = 1;
    private final String CONTENT_AUTHORITY = "com.potatofly";
    private final String CONTENT_TYPE = "com.potatofly.dm";
    @Override
    public void onCreate()
    {

        /*Account account = AccountService.GetAccount(CONTENT_TYPE);
        AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
        if (accountManager != null && accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
            ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            ContentResolver.requestSync(account, CONTENT_AUTHORITY, new Bundle());
        }*/
        Log.e("sdk","== LiveService onCreate");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LiveJobService.StartJob(this);
        }else {
            PendingIntent _PendingIntent = PendingIntent.getService(this, 0, new Intent(this, LiveService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager _AlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (_AlarmManager != null) {
                _AlarmManager.cancel(_PendingIntent);
                _AlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL, _PendingIntent);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("sdk","== LiveService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
