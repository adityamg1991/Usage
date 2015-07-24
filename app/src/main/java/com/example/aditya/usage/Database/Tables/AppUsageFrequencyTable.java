package com.example.aditya.usage.Database.Tables;

/**
 * Created by aditya on 13/07/15.
 */
public class AppUsageFrequencyTable {

    // Table name
    public static final String TABLE_NAME = "app_usage_table";

    // Columns
    public static final String ID = "_id";
    // App name
    public static final String LABEL = "label";
    // Package Name (Unique)
    public static final String PACKAGE_NAME = "package_name";
    // How many times user has opened the app
    public static final String FREQUENCY = "frequency";
    // When did user last opened the app (In millis)
    public static final String LAST_USED = "last_used";
    // Average time user spends in the app
    public static final String AVG_USAGE_TIME = "avg_user_time";
    // Total time user has spent in the app
    public static final String TOTAL_TIME = "total_time";
    // After installation of usage, when was the first time user opened the app (String in database)
    public static final String FIRST_USED = "first_used";

    // Create table command
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LABEL + " TEXT, " +
            PACKAGE_NAME + " Text, " + FREQUENCY + " INTEGER, " + LAST_USED + " INTEGER, " +
            AVG_USAGE_TIME + " REAL, " + TOTAL_TIME + " REAL, " + FIRST_USED + " INTEGER )";

}
