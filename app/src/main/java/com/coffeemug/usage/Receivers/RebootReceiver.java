package com.coffeemug.usage.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.coffeemug.usage.Service.CheckForegroundAppService;

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
