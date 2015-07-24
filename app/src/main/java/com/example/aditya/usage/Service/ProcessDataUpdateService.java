package com.example.aditya.usage.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.aditya.usage.Data.ProcessData;
import com.example.aditya.usage.Event.ProcessInfoEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ProcessDataUpdateService extends Service {

    private static final String TAG = "Service";
    private final Handler handler = new Handler();
    private final ProcessDataRunnable processDataRunnable = new ProcessDataRunnable();
    private static final int INTERVAL = 5 * 1000;
    private ActivityManager managerActivity;
    private PackageManager managerPackage;

    public ProcessDataUpdateService() {
    }


    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onCreate() {
        super.onCreate();
        handler.post(processDataRunnable);
        managerActivity = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        managerPackage = getApplicationContext().getPackageManager();
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    public void onDestroy() {
        handler.removeCallbacks(processDataRunnable);
        super.onDestroy();
    }


    private class ProcessDataRunnable implements Runnable {
        @Override
        public void run() {

            new AsyncTask<Void, Void, ArrayList<ProcessData>>() {

                protected ArrayList<ProcessData> doInBackground(Void... params) {
                    return getLatestProcessInfo();
                }

                protected void onPostExecute(ArrayList<ProcessData> list) {
                    super.onPostExecute(list);

                    ProcessInfoEvent event = new ProcessInfoEvent();
                    event.setProcessDataList(list);
                    EventBus.getDefault().post(event);

                    handler.postDelayed(processDataRunnable, INTERVAL);
                }

            }.execute();
        }
    }


    private ArrayList<ProcessData> getLatestProcessInfo() {

        final ArrayList<ProcessData> listUsageProcess = new ArrayList<ProcessData>();

        if(null != managerActivity) {

            List<ActivityManager.RunningAppProcessInfo> listRunningProcesses = managerActivity
                    .getRunningAppProcesses();

            for (int i = 0; i < listRunningProcesses.size(); i++) {

                ActivityManager.RunningAppProcessInfo unit = listRunningProcesses.get(i);
                ProcessData unitProcessData = new ProcessData();

                Debug.MemoryInfo memInfo[] = managerActivity.getProcessMemoryInfo(new int[]{unit.pid});
                float memInMB = memInfo[0].getTotalPss() / 1000;

                try {
                    unitProcessData.setIconDrawable(managerPackage.getApplicationIcon(unit.processName));
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d(TAG, "Package not found");
                }

                unitProcessData.setMemInMB(memInMB);
                unitProcessData.setPackageName(unit.processName);
                unitProcessData.setProcessId(unit.pid);

                ApplicationInfo appInfo = null;
                try {
                    appInfo = managerPackage.getApplicationInfo(unit.processName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d(TAG, "App name not found");
                }

                if(null != appInfo) {

                    // App has a name
                    unitProcessData.setIsUnknown(false);
                    unitProcessData.setAppName((String) managerPackage.getApplicationLabel(appInfo));
                    if(isUserApp(appInfo)) {
                        unitProcessData.setIsSystemApp(false);
                    } else {
                        unitProcessData.setIsSystemApp(true);
                    }
                } else {

                    // App doesn't have a name, it is unknown.
                    unitProcessData.setIsUnknown(true);
                }

                listUsageProcess.add(unitProcessData);
            }
        }

        return listUsageProcess;
    }


    boolean isUserApp(ApplicationInfo ai) {
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (ai.flags & mask) == 0;
    }
}
