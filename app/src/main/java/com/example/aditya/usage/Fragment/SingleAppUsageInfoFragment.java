package com.example.aditya.usage.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aditya.usage.Database.Data.AppUsageFrequencyTableItem;
import com.example.aditya.usage.Database.DatabaseHelper;
import com.example.aditya.usage.Database.Tables.AppUsageFrequencyTable;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;
import com.example.aditya.usage.Utilities.UsageApplication;
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

    DatabaseHelper dbHelper;
    private static final String TAG = "SingleAppUsageInfo";

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


        String packageName = getArguments().getString(Constants.PCK_NAME);
        if(null == packageName) {
            getActivity().finish();
            Toast.makeText(getActivity(), "Some error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = dbHelper.getPackageInfo(packageName);
        ArrayList<AppUsageFrequencyTableItem> list
                = UsageApplication.getAppUsageFrequencyTableData(cursor);

        if(list.size() != 1) {
            return;
        }

        AppUsageFrequencyTableItem item = list.get(0);

        long frequency = item.getFrequency();
        String lastUsed = item.getLastUsed();
        String firstUsed = item.getFirstUsed();
        double avgUsageTime = item.getAverageUseTime();
        double totalTime = item.getTotalUseTime();
        String appLabel = item.getLabel();

        double totalPhoneUsage = dbHelper.getTotalPhoneUsageInSeconds();
        totalPhoneUsage -= totalTime;

        String formattedTotalUsage = UsageApplication.getFormattedUsageTime(totalTime);
        tvTotalTimeUsed.setText(formattedTotalUsage);

        try {
            long longLastUsed = Long.parseLong(lastUsed);
            String lastTimeUsed = UsageApplication.getFormattedTime(longLastUsed);
            tvLastUsed.setText(lastTimeUsed);
        } catch(Exception e) {
            e.printStackTrace();
        }

        String strAverageTime = UsageApplication.getFormattedUsageTime(avgUsageTime);
        tvAverageDuration.setText(strAverageTime);

        try {
            Date initialDate = new Date(Long.parseLong(firstUsed));
            Date lastDate = new Date(Long.parseLong(lastUsed));

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
}
