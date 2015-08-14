package com.coffeemug.usage.Receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.coffeemug.usage.Service.ManageDataIntentService;

/**
 * Created by aditya on 22/07/15.
 */
public class ManageDataReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ManageDataReceiver", "From BR : Starting Managing Data");
        Intent service = new Intent(context, ManageDataIntentService.class);
        startWakefulService(context, service);
    }
}
