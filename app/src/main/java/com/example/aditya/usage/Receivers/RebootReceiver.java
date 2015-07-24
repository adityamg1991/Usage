package com.example.aditya.usage.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.aditya.usage.Activity.MainActivity;
import com.example.aditya.usage.Service.CheckForegroundAppService;
import com.example.aditya.usage.Utilities.UsageApplication;

/**
 * Created by aditya on 16/07/15.
 */
public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, CheckForegroundAppService.class);
        context.startService(serviceIntent);
    }
}
