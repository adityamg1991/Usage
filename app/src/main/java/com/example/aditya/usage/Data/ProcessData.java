package com.example.aditya.usage.Data;

import android.graphics.drawable.Drawable;

/**
 * Created by aditya on 01/07/15.
 */
public class ProcessData {

    private int processId;
    private String packageName;
    private float memInMB;
    private Drawable iconDrawable;
    private boolean isSystemApp;
    private boolean isUnknown;

    public boolean isUnknown() {
        return isUnknown;
    }

    public void setIsUnknown(boolean isUnknown) {
        this.isUnknown = isUnknown;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private String appName;

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int pID) {
        this.processId = pID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public float getMemInMB() {
        return memInMB;
    }

    public void setMemInMB(float memInMB) {
        this.memInMB = memInMB;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
}
