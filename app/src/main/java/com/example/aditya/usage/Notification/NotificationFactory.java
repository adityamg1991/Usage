package com.example.aditya.usage.Notification;

import android.app.NotificationManager;
import android.content.Context;

import com.example.aditya.usage.Data.NotificationData;

/**
 * Created by aditya on 27/07/15.
 */
public class NotificationFactory {

    public static final int TYPE_UNUSED_APPS = 0;
    private static NotificationFactory instance;
    private static NotificationManager managerNotification;
    private static Context mContext;

    private NotificationFactory() {

    }


    public static NotificationFactory getInstance(Context con) {

        if(null == instance) {
            instance = new NotificationFactory();
            managerNotification = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
            mContext = con;
        }
        return instance;
    }


    public void shootNotification(NotificationData data) {

        UsageNotification notification = null;

        if (null != data) {

            if(data.getNotificationType() == TYPE_UNUSED_APPS) {
                notification = new NotificationUnusedApps(data, mContext);
            }

            showNotification(notification);
        }
    }


    private void showNotification(UsageNotification notification) {

        if(null == notification) {
            return;
        }

        managerNotification.notify(0, notification.getNotificationBuilder().build());
    }


    public void cancelAllNotification() {

        managerNotification.cancelAll();
    }
}
