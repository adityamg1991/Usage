package com.coffeemug.usage.Database.Tables;

/**
 * Created by aditya on 22/07/15.
 */
public class PastAppData {

    public static final String TABLE_NAME = "past_app_data";

    public static final String ID = "_id";
    public static final String DATE = "date";
    public static final String APP_DATA = "app_data";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT, " + APP_DATA + " TEXT )";
}
