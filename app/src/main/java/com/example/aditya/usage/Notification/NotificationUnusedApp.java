package com.example.aditya.usage.Notification;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.example.aditya.usage.Data.NotificationData;
import com.example.aditya.usage.R;

/**
 * Created by aditya on 27/07/15.
 */
public class NotificationUnusedApp extends UsageNotification {

    private NotificationData globalData;
    private Context mContext;

    private final String TITLE = "Unused app found. Remove it to save space and memory?";

    private NotificationUnusedApp() {

    }

    public NotificationUnusedApp(NotificationData data, Context con) {
        this.globalData = data;
        this.mContext = con;
    }

    @Override
    public NotificationCompat.Builder getNotificationBuilder() {

        String appLabel = globalData.getAppLabel();
        if(TextUtils.isEmpty(appLabel)) {
            appLabel = globalData.getPackageName();
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(TITLE)
                        .setContentText(appLabel);

        return mBuilder;
    }
}
