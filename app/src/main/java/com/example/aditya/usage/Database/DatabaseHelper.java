package com.example.aditya.usage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aditya.usage.Data.AppUsageFrequencyTableItem;
import com.example.aditya.usage.Data.PhoneFactItem;
import com.example.aditya.usage.Database.Tables.AppUsageFrequencyTable;
import com.example.aditya.usage.Database.Tables.PastAppData;
import com.example.aditya.usage.Utilities.Constants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aditya on 06/07/15.
 */
public class DatabaseHelper {

    private static final String DATABASE_NAME = "USAGE_DATABASE";
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;
    private static PrivateDatabaseHelper innerInstance;
    private static SQLiteDatabase database;

    private DatabaseHelper(){}


    public static DatabaseHelper getInstance(Context con) {

        if(null == innerInstance) {
            innerInstance = new PrivateDatabaseHelper(con, DATABASE_NAME, null, DATABASE_VERSION);
        }

        if(null == database) {
            database = innerInstance.getWritableDatabase();
        }

        if(null == instance) {
            instance = new DatabaseHelper();
        }

        return instance;
    }


    private static class PrivateDatabaseHelper extends SQLiteOpenHelper {

        public PrivateDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            log(AppUsageFrequencyTable.CREATE_TABLE);
            db.execSQL(AppUsageFrequencyTable.CREATE_TABLE);

            log(PastAppData.CREATE_TABLE);
            db.execSQL(PastAppData.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    public void addPastAppDataEntry(JSONArray jArray) {

        Date d = new Date();
        String formattedDate = Constants.APP_DATE_FORMAT.format(d);

        ContentValues cv = new ContentValues();
        cv.put(PastAppData.DATE, formattedDate);
        cv.put(PastAppData.APP_DATA, jArray.toString());

        log("Past App Data insertion: " + cv.toString());
        database.insert(PastAppData.TABLE_NAME, null, cv);
    }


    public void updateFrequencyTable(AppUsageFrequencyTableItem item, long timeUsed) {

        String pck = item.getPackageName();
        String label = item.getLabel();

        if(isPackagePresent(pck)) {
            updatePackage(pck, timeUsed);
        } else {
            insertPackage(pck, label, timeUsed);
        }
    }


    public Cursor getPackageInfo(String packageName) {

        String strSelectApps = "SELECT * FROM " + AppUsageFrequencyTable.TABLE_NAME +
                " WHERE " + AppUsageFrequencyTable.PACKAGE_NAME + " LIKE " + "'" + packageName + "'";
        log(strSelectApps);
        Cursor cursor = database.rawQuery(strSelectApps, null);
        return cursor;
    }


    public Cursor getAppUsageCursor() {

        String strSelectApps = "SELECT * FROM " + AppUsageFrequencyTable.TABLE_NAME +
                " ORDER BY " + AppUsageFrequencyTable.TOTAL_TIME + " DESC";
        log(strSelectApps);
        Cursor cursor = database.rawQuery(strSelectApps, null);
        return cursor;
    }


    /**
     * Apps which haven't been used since timeInMillis are returned
     */
    public Cursor getAppsNotUsedInTime(long timeInMillis) {

        Cursor cursor = null;
        long timeBefore = System.currentTimeMillis();
        timeBefore -= timeInMillis;

        String sql = "SELECT * FROM " + AppUsageFrequencyTable.TABLE_NAME +
                " WHERE " + AppUsageFrequencyTable.LAST_USED + " < " + timeBefore;
        log(sql);
        cursor = database.rawQuery(sql, null);

        return cursor;
    }




    public void clearAppUsageTable() {
        String query = "DELETE FROM " + AppUsageFrequencyTable.TABLE_NAME;
        log(query);
        database.execSQL(query);
    }


    public long getTotalPhoneUsageInSeconds() {

        long totalTime = 0;

        String strSelectApps = "SELECT * FROM " + AppUsageFrequencyTable.TABLE_NAME;
        log(strSelectApps);

        Cursor cursor = database.rawQuery(strSelectApps, null);
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            totalTime += cursor.getLong(cursor.getColumnIndex(AppUsageFrequencyTable.TOTAL_TIME));
        }

        return totalTime;
    }


    private void updatePackage(String pck, long timeUsed) {

        String getFrequencyQuery = "SELECT * FROM " + AppUsageFrequencyTable.TABLE_NAME + " WHERE " + AppUsageFrequencyTable
                .PACKAGE_NAME  + " LIKE " + "'" + pck + "'";
        log(getFrequencyQuery);

        Cursor cursor = database.rawQuery(getFrequencyQuery, null);

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            // Updating frequency
            int freq = cursor.getInt(cursor.getColumnIndex(AppUsageFrequencyTable.FREQUENCY));
            freq++;

            // Updating total time
            double totalTime = cursor.getInt(cursor.getColumnIndex(AppUsageFrequencyTable.TOTAL_TIME));
            totalTime += timeUsed;

            // Updating average time
            double avgTime = cursor.getDouble(cursor.getColumnIndex(AppUsageFrequencyTable.AVG_USAGE_TIME));
            avgTime = ((avgTime*(freq - 1)) + timeUsed) / freq;

            long currentTime = System.currentTimeMillis();

            String updateFreqQuery = "UPDATE " + AppUsageFrequencyTable.TABLE_NAME + " SET " +
                    AppUsageFrequencyTable.FREQUENCY + " = " + freq + ", " +
                    AppUsageFrequencyTable.TOTAL_TIME + " = " + totalTime + ", " +
                    AppUsageFrequencyTable.LAST_USED + " = " + currentTime + ", " +
                    AppUsageFrequencyTable.AVG_USAGE_TIME + " = " + avgTime +
                    " WHERE " + AppUsageFrequencyTable.PACKAGE_NAME + " LIKE " + "'" + pck + "'";

            log(updateFreqQuery);
            database.execSQL(updateFreqQuery);
         }
    }


    private boolean isPackagePresent(String pck) {

        String checkQuery = "SELECT " + AppUsageFrequencyTable.ID + " FROM " +
                AppUsageFrequencyTable.TABLE_NAME + " WHERE " + AppUsageFrequencyTable
                .PACKAGE_NAME + " LIKE " + "'" + pck + "'";
        log(checkQuery);

        Cursor cursor = database.rawQuery(checkQuery, null);
        if(cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }


    private void insertPackage(String pck, String label, long timeUsed) {

        ContentValues cv = new ContentValues();
        cv.put(AppUsageFrequencyTable.PACKAGE_NAME, pck);
        cv.put(AppUsageFrequencyTable.LABEL, label);
        cv.put(AppUsageFrequencyTable.FREQUENCY, 1);
        cv.put(AppUsageFrequencyTable.LAST_USED, System.currentTimeMillis());
        cv.put(AppUsageFrequencyTable.AVG_USAGE_TIME, timeUsed);
        cv.put(AppUsageFrequencyTable.TOTAL_TIME, timeUsed);
        cv.put(AppUsageFrequencyTable.FIRST_USED, System.currentTimeMillis());

        log("Content Values to be put : " + cv.toString());
        database.insert(AppUsageFrequencyTable.TABLE_NAME, null, cv);
    }


    private static void log(String query) {
        Log.d(TAG, query);
    }


    private ArrayList<PhoneFactItem> getPhoneFacts() {

        ArrayList<PhoneFactItem> list = null;

        String query = "SELECT * FROM " + AppUsageFrequencyTable.TABLE_NAME
                + " ORDER BY " + AppUsageFrequencyTable.TOTAL_TIME + " DESC LIMIT 1";
        log(query);

        return list;
    }
}
