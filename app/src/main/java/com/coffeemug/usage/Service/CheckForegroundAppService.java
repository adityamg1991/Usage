package com.coffeemug.usage.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.coffeemug.usage.Data.AppUsageFrequencyTableItem;
import com.coffeemug.usage.Data.LastOpenedApp;
import com.coffeemug.usage.Database.DatabaseHelper;
import com.coffeemug.usage.Utilities.Constants;
import com.coffeemug.usage.Utilities.UsageApplication;

import java.util.List;

public class CheckForegroundAppService extends Service {

    private final String TAG = "CheckForegroundService";
    private final int INTERVAL = 1 * 1000;
    private final Handler handler = new Handler();
    private final CheckForegroundAppRunnable checkForegroundAppRunnable = new
            CheckForegroundAppRunnable();

    private ActivityManager managerActivity;
    private PackageManager managerPackage;

    private DatabaseHelper databaseHelper;

    private long time;

    public CheckForegroundAppService() {
    }


    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    public void onCreate() {
        super.onCreate();
        managerActivity = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        handler.post(checkForegroundAppRunnable);
        managerPackage = getApplicationContext().getPackageManager();
        databaseHelper = DatabaseHelper.getInstance(this);
        time = 0;
    }


    private class CheckForegroundAppRunnable implements Runnable {

        public void run() {
            checkAppInForeground();
            handler.postDelayed(checkForegroundAppRunnable, INTERVAL);
        }
    }


    private void checkAppInForeground() {

        List<ActivityManager.RunningAppProcessInfo> appProcesses = managerActivity.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcess : appProcesses){

            if(appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                analyseForegroundApp(appProcess.processName);
                break;
            }
        }
    }


    private void analyseForegroundApp(String packageName) {

        String lastAppPackage = LastOpenedApp.getInstance().getPackageName();

        if(null != lastAppPackage) {

            if(!lastAppPackage.equalsIgnoreCase(packageName)) {

                time++;
                Log.d(TAG, lastAppPackage + " : " + time + " secs");
                updateDatabase(lastAppPackage, getAppName(lastAppPackage), time);
                LastOpenedApp.getInstance().setData(packageName);
                time = 0;

            } else {
                LastOpenedApp.getInstance().setData(packageName);
                time++;
            }
        } else {
            LastOpenedApp.getInstance().setData(packageName);
        }
    }


    private void updateDatabase(String pck, String label, long timeUsed) {

        AppUsageFrequencyTableItem unit = new AppUsageFrequencyTableItem();
        unit.setPackageName(pck);

        if(null != label) {
                unit.setLabel(label);
            } else {
                unit.setLabel("");
        }

        // Don't enter in database if it's home launcher or usage app
        if(!pck.equals(UsageApplication.getHomePackage())
                && !pck.equals(getApplication().getPackageName())) {

            if(!Constants.PHONE_LAUNCHER_PACKAGE_LIST.contains(pck)) {

                databaseHelper.updateFrequencyTable(unit, timeUsed);
            }
        }
    }


    public void onDestroy() {
        handler.removeCallbacks(checkForegroundAppRunnable);
        super.onDestroy();
    }


    /**
     * Get App label from package name
     * @param packageName
     * @return
     */
    private String getAppName(String packageName) {

        ApplicationInfo appInfo = null;
        try {
            appInfo = managerPackage.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "App name not found");

        }
        if (null != appInfo) {
            return managerPackage.getApplicationLabel(appInfo).toString();
        } else {
            return null;
        }
    }
}
