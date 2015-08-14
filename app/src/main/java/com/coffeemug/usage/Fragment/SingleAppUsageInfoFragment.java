package com.coffeemug.usage.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.coffeemug.usage.Data.AppUsageFrequencyTableItem;
import com.coffeemug.usage.Database.DatabaseHelper;
import com.coffeemug.usage.Utilities.Constants;
import com.coffeemug.usage.Utilities.UsageApplication;
import com.coffeemug.usage.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by aditya on 16/07/15.
 */
public class SingleAppUsageInfoFragment extends Fragment implements View.OnClickListener{

    private DatabaseHelper dbHelper;
    private static final String TAG = "SingleAppUsageInfo";
    private String packageName;
    private TextView tvTotalTimeUsed, tvLastUsed, tvAverageDailyUsage, tvAverageDuration;

    public SingleAppUsageInfoFragment(){}

    public static SingleAppUsageInfoFragment newInstance(String pckName) {

        SingleAppUsageInfoFragment infoFragment = new SingleAppUsageInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.PCK_NAME, pckName);
        infoFragment.setArguments(bundle);

        return infoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_app_usage_info, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = DatabaseHelper.getInstance(getActivity());

        getActivity().findViewById(R.id.cv_total_time).setOnClickListener(this);
        getActivity().findViewById(R.id.cv_last_used).setOnClickListener(this);
        getActivity().findViewById(R.id.cv_avg_open_open_count).setOnClickListener(this);
        getActivity().findViewById(R.id.cv_avg_usage_duration).setOnClickListener(this);


        tvTotalTimeUsed = (TextView) getActivity().findViewById(R.id.tv_total_time_used);
        tvLastUsed = (TextView) getActivity().findViewById(R.id.tv_last_used);
        tvAverageDailyUsage = (TextView) getActivity().findViewById(R.id.tv_avg_daily_freq);
        tvAverageDuration = (TextView) getActivity().findViewById(R.id.tv_avg_use_duration);


        packageName = getArguments().getString(Constants.PCK_NAME);
        if(TextUtils.isEmpty(packageName)) {
            Toast.makeText(getActivity(), "Package name not found", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }

        Log.d(TAG, "Received Package Name : " + packageName);
        Cursor cursor = dbHelper.getPackageInfo(packageName);
        ArrayList<AppUsageFrequencyTableItem> list
                = UsageApplication.getAppUsageFrequencyTableData(cursor);

        LinearLayout llContainer = (LinearLayout) getActivity().findViewById(R.id.ll_single_app_usage_container);
        if(list.size() != 1) {
            Snackbar.make(llContainer, "Entry not found in Database. Probably, this app has never been opened.", Snackbar.LENGTH_LONG)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().finish();
                        }
                    })
                    .show();
            return;
        }

        AppUsageFrequencyTableItem item = list.get(0);

        long frequency = item.getFrequency();
        long lastUsed = item.getLastUsed();
        long firstUsed = item.getFirstUsed();
        double avgUsageTime = item.getAverageUseTime();
        double totalTime = item.getTotalUseTime();
        String appLabel = item.getLabel();

        double totalPhoneUsage = dbHelper.getTotalPhoneUsageInSeconds();
        totalPhoneUsage -= totalTime;

        String formattedTotalUsage = UsageApplication.getFormattedUsageTime(totalTime);
        tvTotalTimeUsed.setText(formattedTotalUsage);

        try {
            String lastTimeUsed = UsageApplication.getFormattedTime(lastUsed);
            tvLastUsed.setText(lastTimeUsed);
        } catch(Exception e) {
            e.printStackTrace();
        }

        String strAverageTime = UsageApplication.getFormattedUsageTime(avgUsageTime);
        tvAverageDuration.setText(strAverageTime);

        try {
            Date initialDate = new Date(firstUsed);
            Date lastDate = new Date(lastUsed);

            Log.d(TAG, "FIRST_USE_DATE : " + initialDate.toString());
            Log.d(TAG, "LAST_USE_DATE : " + lastDate.toString());

            long diff = lastDate.getTime() - initialDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if(0 == days) {

                tvAverageDailyUsage.setText(frequency + " Times");
            } else {
                double averageDailyUse = frequency / days;
                averageDailyUse = UsageApplication.round(averageDailyUse, 2);
                tvAverageDailyUsage.setText(averageDailyUse + " Times");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        PieChart pieChart = (PieChart) getActivity().findViewById(R.id.pie_chart);

        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry((float) totalTime, 0));
        entries.add(new Entry((float) totalPhoneUsage, 1));

        if(TextUtils.isEmpty(appLabel)) {
            labels.add(packageName);
        } else {
            labels.add(appLabel);
        }
        labels.add("Rest of the apps");

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(labels, pieDataSet);
        pieChart.setData(pieData);

        pieChart.setDescription("Values in Percentage");
        pieChart.setDescriptionTextSize(UsageApplication.pxFromDp(getActivity(), 3));
        pieChart.setDescriptionColor(getActivity().getResources().getColor(R.color.app_theme_blue));

        pieChart.setUsePercentValues(true);
        pieChart.setDrawCenterText(true);

        pieChart.getLegend().setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        getActivity().invalidateOptionsMenu();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cv_total_time : {
                showInfoDialog("Total time you have spent in this app after installing Usage.",
                        tvTotalTimeUsed.getText().toString());
                break;
            }
            case R.id.cv_last_used : {
                showInfoDialog("When did you last use this app.",
                        tvLastUsed.getText().toString());
                break;
            }
            case R.id.cv_avg_open_open_count : {
                showInfoDialog("On an average, how many times you open this app in a day.",
                        tvAverageDailyUsage.getText().toString());
                break;
            }
            case R.id.cv_avg_usage_duration : {
                showInfoDialog("On an average, how much time you spend in this app once you open it.",
                        tvAverageDuration.getText().toString());
                break;
            }
        }
    }


    private void showInfoDialog(String text, String title) {

        new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(text)
                .positiveText("Got it")
                .show();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "Menu prepared");

        MenuItem item = menu.findItem(R.id.action_open_app);
        try {
            Drawable icon = getActivity().getPackageManager().getApplicationIcon(packageName);
            item.setIcon(icon);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "Menu created");
        inflater.inflate(R.menu.menu_single_app_usage_info, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_open_app) {
            openAppSettings();
        }

        return super.onOptionsItemSelected(item);
    }


    private void openAppSettings() {

        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);

        } catch ( ActivityNotFoundException e ) {
            e.printStackTrace();
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
    }
}
