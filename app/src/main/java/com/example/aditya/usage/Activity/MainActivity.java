package com.example.aditya.usage.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.aditya.usage.Fragment.AppStatisticsFragment;
import com.example.aditya.usage.Fragment.MemoryUsageFragment;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Receivers.ManageDataReceiver;
import com.example.aditya.usage.Utilities.Constants;

import java.util.Calendar;


public class MainActivity extends BaseActivity {

    private final String FRAG_TAG = "frag_tag";
    private static final String TAG = "MainActivity";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager managerFragment = getSupportFragmentManager();
        if(null == managerFragment.findFragmentByTag(FRAG_TAG)) {

            managerFragment.beginTransaction().
                    add(android.R.id.content, AppStatisticsFragment.newInstance(), FRAG_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_open_app_usage_activity : {
                startActivity(new Intent(this, AppUsagePatternActivity.class));
                break;
            }
            case R.id.action_open_memory_usage_activity : {
                startActivity(new Intent(this, MemoryUsageActivity.class));
                break;
            }
            case R.id.action_open_battery_usage_activity : {
                openBatteryScreen();
                break;
            }
            case R.id.action_open_futile_apps : {
                openFutileApps();
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }


    private void openFutileApps() {

        Intent i = new Intent(this, AppUsagePatternActivity.class);
        i.putExtra(Constants.KEY_SHOW_FUTILE_APPS, Constants.VALUE_SHOW_FUTILE_APPS);
        startActivity(i);
    }


    private void openBatteryScreen() {
        try {
            Intent intentBatteryUsage = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            startActivity(intentBatteryUsage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
