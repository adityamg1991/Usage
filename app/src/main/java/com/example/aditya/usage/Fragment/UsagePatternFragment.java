package com.example.aditya.usage.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.aditya.usage.Adapter.AppUsagePatternAdapter;
import com.example.aditya.usage.Database.DatabaseHelper;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;

/**
 * Created by aditya on 10/07/15.
 */
public class UsagePatternFragment extends Fragment {

    ListView lvAppUsagePattern;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.usage_pattern_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvAppUsagePattern = (ListView) getActivity().findViewById(R.id.lv_app_usage);

        Bundle bundle = getArguments();

        if( bundle != null && bundle.containsKey(Constants.KEY_SHOW_FUTILE_APPS)
                && bundle.getString(Constants.KEY_SHOW_FUTILE_APPS).equals(Constants.VALUE_SHOW_FUTILE_APPS)) {


        } else {

            Cursor cursor = DatabaseHelper.getInstance(getActivity()).getAppUsageCursor();

            if(0 == cursor.getCount()) {
                getActivity().findViewById(R.id.tv_no_apps_found).setVisibility(View.VISIBLE);
            } else {
                AppUsagePatternAdapter adapter = new AppUsagePatternAdapter(getActivity(), cursor, true);
                lvAppUsagePattern.setAdapter(adapter);
            }
        }
    }
}
