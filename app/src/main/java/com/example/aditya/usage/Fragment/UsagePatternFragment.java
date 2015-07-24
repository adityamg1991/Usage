package com.example.aditya.usage.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aditya.usage.Adapter.AppUsagePatternAdapter;
import com.example.aditya.usage.Database.DatabaseHelper;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;

/**
 * Created by aditya on 10/07/15.
 */
public class UsagePatternFragment extends Fragment {

    ListView lvAppUsagePattern;
    TextView tvNoData;
    DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.usage_pattern_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbHelper = DatabaseHelper.getInstance(getActivity());
        lvAppUsagePattern = (ListView) getActivity().findViewById(R.id.lv_app_usage);
        tvNoData = (TextView) getActivity().findViewById(R.id.tv_no_apps_found);

        Bundle bundle = getArguments();

        Cursor cursor = null;

        if( bundle != null && bundle.containsKey(Constants.KEY_SHOW_FUTILE_APPS)
                && bundle.getString(Constants.KEY_SHOW_FUTILE_APPS).equals(Constants.VALUE_SHOW_FUTILE_APPS)) {

            // Apps not used in 4 days
            cursor = dbHelper.getAppsNotUsedInTime(4 * 86400 * 1000);
            if(0 == cursor.getCount()) {
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(getActivity().getResources().getString(R.string.no_unused_app_found));
            }
        } else {

            cursor = dbHelper.getAppUsageCursor();
            if(0 == cursor.getCount()) {
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(getActivity().getResources().getString(R.string.no_used_app_found));
            }
        }

        AppUsagePatternAdapter adapter = new AppUsagePatternAdapter(getActivity(), cursor, true);
        lvAppUsagePattern.setAdapter(adapter);
    }
}
