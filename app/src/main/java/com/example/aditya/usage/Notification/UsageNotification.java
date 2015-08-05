package com.example.aditya.usage.Notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.example.aditya.usage.Data.NotificationData;
import com.example.aditya.usage.R;

/**
 * Created by aditya on 27/07/15.
 */
public abstract class UsageNotification {

    public UsageNotification() {
    }

    public abstract NotificationCompat.Builder getNotificationBuilder();
}
