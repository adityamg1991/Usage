package com.example.aditya.usage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aditya.usage.Activity.SingleAppUsageInfoActivity;
import com.example.aditya.usage.Database.DatabaseHelper;
import com.example.aditya.usage.Database.Tables.AppUsageFrequencyTable;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;
import com.example.aditya.usage.Utilities.UsageApplication;


/**
 * Created by aditya on 09/07/15.
 */
public class AppUsagePatternAdapter extends CursorAdapter{

    PackageManager managerPackage;


    public AppUsagePatternAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        managerPackage = context.getPackageManager();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout
                .app_usage_pattern_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_app_logo);
        TextView tvLabel = (TextView) view.findViewById(R.id.tv_label);
        TextView tvFrequency = (TextView) view.findViewById(R.id.tv_frequency);
        TextView tvLastUsed = (TextView) view.findViewById(R.id.tv_last_used);
        RelativeLayout rlContainer = (RelativeLayout) view.findViewById(R.id.rl_container);

        String label = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.LABEL));
        String packageName = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.PACKAGE_NAME));
        String lastUsageTime = cursor.getString(cursor.getColumnIndex(AppUsageFrequencyTable.LAST_USED));
        double avgUsageTimeInSecs = cursor.getDouble(cursor.getColumnIndex(AppUsageFrequencyTable.AVG_USAGE_TIME));

        // Getting app logo
        Drawable logo = null;
        try {
            if(!TextUtils.isEmpty(packageName)) {
                logo = managerPackage.getApplicationIcon(packageName);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(null == logo) {

            String str = "";
            if(!TextUtils.isEmpty(label)) {
                str += label.charAt(0);
            } else if(!TextUtils.isEmpty(packageName)) {
                str += packageName.charAt(0);
            }
            logo = UsageApplication.getRoundedBitmapOfDesiredColor(context, str, Constants.APP_THEME_HASH_CODE);
        }
        ivLogo.setImageDrawable(logo);

        // Set app name, or package name as title
        if(TextUtils.isEmpty(label)) {
            tvLabel.setText(packageName);
        } else {
            tvLabel.setText(label);
        }

        tvFrequency.setText("Average Usage : " +
                UsageApplication.getFormattedUsageTime(avgUsageTimeInSecs));

        try {
            long longLastUsage = Long.parseLong(lastUsageTime);
            tvLastUsed.setText("Last Opened : " + UsageApplication.getFormattedTime(longLastUsage));
        } catch(Exception e) {
            e.printStackTrace();
        }

        rlContainer.setTag(R.id.id_one, packageName);
        rlContainer.setTag(R.id.id_two, label);
        rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String packageName = (String) v.getTag(R.id.id_one);
                String label = (String) v.getTag(R.id.id_two);

                Intent i = new Intent(context, SingleAppUsageInfoActivity.class);
                i.putExtra(Constants.PCK_NAME, packageName);
                i.putExtra(Constants.APP_LABEL, label);
                context.startActivity(i);
            }
        });
    }
}

