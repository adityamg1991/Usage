package com.example.aditya.usage.Data;

/**
 * Created by aditya on 06/07/15.
 */
public class LastOpenedApp {

    private static LastOpenedApp instance;
    private String packageName;

    private LastOpenedApp(){}

    public static LastOpenedApp getInstance() {

        if(null == instance) {
            instance = new LastOpenedApp();
        }
        return instance;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setData(String packageName) {
        this.packageName = packageName;
    }
}
