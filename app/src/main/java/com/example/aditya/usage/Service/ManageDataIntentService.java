package com.example.aditya.usage.Service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.aditya.usage.Database.DatabaseHelper;
import com.example.aditya.usage.Database.Tables.AppUsageFrequencyTable;
import com.example.aditya.usage.Receivers.ManageDataReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

public class ManageDataIntentService extends IntentService {

    DatabaseHelper dbHelper;
    private final String TAG = "ManageDataIntentService";

    public ManageDataIntentService() {
        super("ManageDataIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Starting Database Transaction");
        dbHelper = DatabaseHelper.getInstance(this);

        try {

            Cursor cursor = dbHelper.getAppUsageCursor();

            JSONArray jArray = new JSONArray();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                int frequency = cursor.getInt(cursor.getColumnIndex(AppUsageFrequencyTable.FREQUENCY));
                String lastUsed = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.LAST_USED));
                String firstUsed = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.FIRST_USED));
                double avgUsageTime = cursor.getDouble(cursor.getColumnIndex(AppUsageFrequencyTable.AVG_USAGE_TIME));
                double totalTime = cursor.getDouble(cursor.getColumnIndex(AppUsageFrequencyTable.TOTAL_TIME));
                String appLabel = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.LABEL));
                String packageName = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.PACKAGE_NAME));

                JSONObject object = new JSONObject();

                object.put(AppUsageFrequencyTable.FREQUENCY, frequency);
                object.put(AppUsageFrequencyTable.LAST_USED, lastUsed);
                object.put(AppUsageFrequencyTable.FIRST_USED, firstUsed);
                object.put(AppUsageFrequencyTable.AVG_USAGE_TIME, avgUsageTime);
                object.put(AppUsageFrequencyTable.TOTAL_TIME, totalTime);
                object.put(AppUsageFrequencyTable.LABEL, appLabel);
                object.put(AppUsageFrequencyTable.PACKAGE_NAME, packageName);

                jArray.put(object);
            }
            Log.d(TAG, "Putting in PastAppData Table : " + jArray.toString());

            // Adding this JSONArray to PastAppData table
            dbHelper.addPastAppDataEntry(jArray);
            // Removing
            dbHelper.clearAppUsageTable();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ManageDataReceiver.completeWakefulIntent(intent);
        }
    }
}
