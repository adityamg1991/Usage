package com.example.aditya.usage.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.aditya.usage.Fragment.AppStatisticsFragment;
import com.example.aditya.usage.Fragment.MemoryUsageFragment;
import com.example.aditya.usage.Fragment.UsageBaseFragment;
import com.example.aditya.usage.Fragment.UsagePatternFragment;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Receivers.CheckFutileAppsReceiver;
import com.example.aditya.usage.Utilities.Constants;


public class MainActivity extends ToolBarBaseActivity {

    private static final String TAG = "MainActivity";
    private FragmentManager managerFragment;
    private UsagePatternFragment mostUsedAppsFragment;
    private UsagePatternFragment futileAppsFragment;
    private MemoryUsageFragment memoryUsageFragment;

    private AppStatisticsFragment appStatisticsFragment;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpAlarm();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        managerFragment = getSupportFragmentManager();

        appStatisticsFragment = AppStatisticsFragment.newInstance();

        mostUsedAppsFragment = new UsagePatternFragment();

        memoryUsageFragment = new MemoryUsageFragment();

        futileAppsFragment = new UsagePatternFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_SHOW_FUTILE_APPS, Constants.VALUE_SHOW_FUTILE_APPS);
        futileAppsFragment.setArguments(bundle);

        swapFragment(appStatisticsFragment);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1 : {
                        swapFragment(appStatisticsFragment);
                        break;
                    }
                    case R.id.show_most_used_apps: {
                        swapFragment(mostUsedAppsFragment);
                        break;
                    }
                    case R.id.show_apps_not_used_in_a_while : {
                        swapFragment(futileAppsFragment);
                        break;
                    }
                    case R.id.show_memory_usage : {
                        swapFragment(memoryUsageFragment);
                        break;
                    }
                }

                return false;
            }
        });
    }


    private void setUpAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, CheckFutileAppsReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp) {
            Log.d("Alaram", "Alarm is already active");
        } else {
            Log.d("Alaram", "Setting Alarm");


        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 60 * 1000, alarmIntent);
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

            case R.id.action_rate_app : {
                break;
            }

            case R.id.action_open_battery_usage_activity : {
                openBatteryScreen();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void openBatteryScreen() {
        try {
            Intent intentBatteryUsage = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            startActivity(intentBatteryUsage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void swapFragment(UsageBaseFragment fragment) {

        if(null != managerFragment) {
            managerFragment.beginTransaction().replace(R.id.rl_container, fragment).commit();
        }
    }
}
