package com.example.aditya.usage.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
public class UsagePatternFragment extends UsageBaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView lvAppUsagePattern;
    TextView tvNoData;
    DatabaseHelper dbHelper;

    private static final int LOADER_ID = 0;
    private boolean isShowingFutileApps;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        getLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle bundle) {

        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {

                Cursor cursor;

                if( bundle != null && bundle.containsKey(Constants.KEY_SHOW_FUTILE_APPS)
                        && bundle.getString(Constants.KEY_SHOW_FUTILE_APPS).equals(Constants.VALUE_SHOW_FUTILE_APPS)) {
                    // Apps not used in 4 days
                    cursor = dbHelper.getAppsNotUsedInTime(4 * 86400 * 1000);
                    isShowingFutileApps = true;
                } else {

                    cursor = dbHelper.getAppUsageCursor();
                    isShowingFutileApps = false;
                }
                return cursor;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        AppUsagePatternAdapter adapter = new AppUsagePatternAdapter(getActivity(), data, true);
        lvAppUsagePattern.setAdapter(adapter);

        if(0 == data.getCount()) {

            if(isShowingFutileApps) {
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(getActivity().getResources().getString(R.string.no_unused_app_found));
            } else {
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(getActivity().getResources().getString(R.string.no_used_app_found));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
