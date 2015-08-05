package com.example.aditya.usage.Data;

/**
 * Created by aditya on 27/07/15.
 */
public class NotificationData {


    private String packageName, appLabel;
    private long lastUsed;
    private int NotificationType;

    public NotificationData(String pck, String label, long lastUsed, int notificationType) {
        this.packageName = pck;
        this.appLabel = label;
        this.lastUsed = lastUsed;
        this.NotificationType = notificationType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public int getNotificationType() {
        return NotificationType;
    }
}
