package com.example.aditya.usage.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aditya on 05/08/15.
 */
public class CheckFutileAppsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alaram", "Notification Alarm ran");
    }
}
