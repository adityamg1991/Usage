package com.example.aditya.usage.Utilities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.example.aditya.usage.Database.Data.AppUsageFrequencyTableItem;
import com.example.aditya.usage.Database.Tables.AppUsageFrequencyTable;
import com.example.aditya.usage.Service.CheckForegroundAppService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aditya on 01/07/15.
 */
public class UsageApplication extends Application {

    private static UsageApplication instance;
    private static SimpleDateFormat sdfToday = new SimpleDateFormat("hh:mm a");
    private static final SimpleDateFormat sdfNotToday = new SimpleDateFormat("ccc, MMM dd yyyy");
    private static PackageManager managerPackage;

    private final static String TAG = "UsageApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, CheckForegroundAppService.class);
        startService(intent);
        managerPackage = getPackageManager();
    }


    public static String getHomePackage() {

        // Getting current home launcher package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = managerPackage.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;
        Log.d(TAG, "Home Launcher : " + currentHomePackage);

        return currentHomePackage;
    }


    /**
     * Returns a list of AppUsageFrequencyTableItem.
     * Basically returns rows of that table from cursor
     * @param cursor
     */
    public static ArrayList<AppUsageFrequencyTableItem> getAppUsageFrequencyTableData(Cursor cursor) {

        ArrayList<AppUsageFrequencyTableItem> data = new ArrayList<AppUsageFrequencyTableItem>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            AppUsageFrequencyTableItem item = new AppUsageFrequencyTableItem();

            long frequency = cursor.getLong(cursor.getColumnIndex(AppUsageFrequencyTable.FREQUENCY));
            long lastUsed = cursor.getLong(cursor.getColumnIndex(AppUsageFrequencyTable.LAST_USED));
            long firstUsed = cursor.getLong(cursor.getColumnIndex(AppUsageFrequencyTable.FIRST_USED));

            double avgUsageTime = cursor.getDouble(cursor.getColumnIndex(AppUsageFrequencyTable.AVG_USAGE_TIME));
            double totalTime = cursor.getDouble(cursor.getColumnIndex(AppUsageFrequencyTable.TOTAL_TIME));

            String appLabel = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.LABEL));
            String packageName = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.PACKAGE_NAME));

            item.setLabel(appLabel);
            item.setAverageUseTime(avgUsageTime);
            item.setFirstUsed(firstUsed);
            item.setFrequency(frequency);
            item.setLastUsed(lastUsed);
            item.setTotalUseTime(totalTime);
            item.setPackageName(packageName);

            data.add(item);
        }

        return data;
    }


    public static String getFormattedTime(long timeInMillis) {

        String formattedTime = null;

        long currentTime = System.currentTimeMillis();
        currentTime = currentTime / 1000;
        long timeInSecs = timeInMillis / 1000;

        if(currentTime > timeInSecs){

            long difference = (currentTime - timeInSecs);
            Log.d("Aditya", "Got Difference : " + difference);

            if(difference <= 60) {
                formattedTime = "Few Seconds ago";
            } else if(difference <= 300) {
                formattedTime = "Few Minutes ago";
            } else if(difference <= 3600) {
                int min = (int) difference / 60;
                formattedTime = min + " Minute(s) ago";
            } else if(difference <= 24 * 60 *60) {
                int hour = (int) difference / 3600;
                formattedTime = hour + " Hour(s) ago";
            } else if(difference <= 86400){
                Date d = new Date(timeInMillis);
                formattedTime = sdfToday.format(d);
            } else {
                Date d = new Date(timeInMillis);
                formattedTime = sdfNotToday.format(d);
            }

        } else {
            // Something bad happened.
            Log.d(Constants.TAG_FUCK, "Usage Application : getFormattedTime() ");
        }

        return formattedTime;
    }


    public static String getFormattedUsageTime(double timeInSec) {

        String str = null;

        if(timeInSec < 60) {
            timeInSec = round(timeInSec, 2);
            str = timeInSec + " sec";
        } else if(timeInSec < 3600) {

            int min = (int) timeInSec / 60;
            int sec = (int) timeInSec % 60;
            str = min + " min, " + sec + " sec";
        } else {

            int hour = (int) timeInSec / 3600;
            int min = ((int) timeInSec % 3600) / 60;
            int sec = (int) timeInSec % 60;
            str = hour + " hour, " + min + " min, " + sec + " sec";
        }

        return str;
    }


    public static Double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }


    public static BitmapDrawable getRoundedBitmapOfDesiredColor
            (Context gContext, String text, String colorHashString) {

        // Converting text to Uppercase
        text = text.toUpperCase();

        int size = (int) pxFromDp(gContext, 200f);
        int halfSize = size / 2;

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Resources resources = gContext.getResources();
        Canvas canvas = new Canvas(bitmap);

        Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setColor(Color.parseColor(colorHashString));

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(pxFromDp(gContext, 60));
        paintText.setColor(Color.WHITE);
        Rect bounds = new Rect();
        paintText.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawCircle(halfSize, halfSize, halfSize, paintBackground);
        canvas.drawText(text, x, y, paintText);

        return new BitmapDrawable(resources, bitmap);

    }


    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }


    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }


    public static String getValueWithoutDecimal(double dubbu) {

        DecimalFormat decimalFormat = new DecimalFormat(".");
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setDecimalSeparatorAlwaysShown(false);
        return decimalFormat.format(dubbu);
    }
}
