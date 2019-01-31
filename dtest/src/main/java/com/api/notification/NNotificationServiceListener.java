package com.api.notification;


import android.graphics.Color;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import static android.app.Notification.FLAG_AUTO_CANCEL;
import static android.app.Notification.VISIBILITY_PRIVATE;


public class NNotificationServiceListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.e("sdk"," open" + "-----" + sbn.getPackageName()+sbn.getNotification().flags);
        if ("com.github.shadowsocks".equals(sbn.getPackageName()) || "com.android.vending".equals(sbn.getPackageName()) || "com.android.providers.downloads".equals(sbn.getPackageName())) {
            Log.e("sdk"," open" + "--cancelNotification--- to remove ");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                cancelNotification(sbn.getKey());
//            } else {
//                cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
//            }
//            sbn.getOverrideGroupKey()

//            sbn.getNotification().flags = 0;
//            sbn.getNotification().color = Color.TRANSPARENT;
//            snoozeNotification(sbn.getKey(),1000000);
            cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
            cancelNotification(sbn.getKey());


//            cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
//            cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e("sdk"," remove" + "-----" + sbn.getPackageName());
    }


    /*private void setNotificationsEnabled(boolean enabled) {
        String packageName = mAppEntry.info.packageName;
        INotificationManager nm = INotificationManager.Stub.asInterface(
                ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        try {
            final boolean enable = mNotificationSwitch.isChecked();
            nm.setNotificationsEnabledForPackage(packageName, mAppEntry.info.uid, enabled);
        } catch (android.os.RemoteException ex) {
            mNotificationSwitch.setChecked(!enabled); // revert
        }
    }*/
}
