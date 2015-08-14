package com.coffeemug.usage.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.coffeemug.usage.Fragment.AppStatisticsFragment;
import com.coffeemug.usage.Fragment.MemoryUsageFragment;
import com.coffeemug.usage.Fragment.UsageBaseFragment;
import com.coffeemug.usage.Fragment.UsagePatternFragment;
import com.coffeemug.usage.Notification.NotificationFactory;
import com.coffeemug.usage.R;
import com.coffeemug.usage.Receivers.CheckFutileAppsReceiver;
import com.coffeemug.usage.Utilities.Constants;


public class MainActivity extends ToolBarBaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentManager managerFragment;
    private UsagePatternFragment mostUsedAppsFragment;
    private UsagePatternFragment futileAppsFragment;
    private MemoryUsageFragment memoryUsageFragment;
    private AppStatisticsFragment appStatisticsFragment;

    private static final int POSITION_FUTILE_FRAGMENT = 2;

    private NavigationView navigationView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1: {
                        swapFragment(appStatisticsFragment);
                        break;
                    }
                    case R.id.show_most_used_apps: {
                        swapFragment(mostUsedAppsFragment);
                        break;
                    }
                    case R.id.show_apps_not_used_in_a_while: {
                        swapFragment(futileAppsFragment);
                        break;
                    }
                    case R.id.show_memory_usage: {
                        swapFragment(memoryUsageFragment);
                        break;
                    }
                }

                return false;
            }
        });

        setUpAlarm();
        checkIfFromNotification();
        cancelAnyPresentNotification();
    }


    private void cancelAnyPresentNotification() {

        NotificationFactory.getInstance(this).cancelAllNotification();
    }


    private void checkIfFromNotification() {

        Intent intent = getIntent();
        if(null != intent) {

            if(intent.hasExtra(Constants.INTENT_KEY)) {
                if(intent.getStringExtra(Constants.INTENT_KEY).equals(Constants.NOTIFICATION_FUTILE_APPS)) {
                    swapFragment(futileAppsFragment);
                    navigationView.getMenu().getItem(POSITION_FUTILE_FRAGMENT).setChecked(true);
                }
            }
        }
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

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
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

            case R.id.action_rate_app : {
                launchMarket();
                break;
            }

            case R.id.action_open_battery_usage_activity : {
                openBatteryScreen();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.couldnt_launch_market, Toast.LENGTH_SHORT).show();
        }
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
