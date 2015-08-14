package com.coffeemug.usage.Notification;

import android.support.v4.app.NotificationCompat;

/**
 * Created by aditya on 27/07/15.
 */
public abstract class UsageNotification {

    public UsageNotification() {
    }

    public abstract NotificationCompat.Builder getNotificationBuilder();
}
