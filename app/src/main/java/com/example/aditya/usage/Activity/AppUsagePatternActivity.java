package com.example.aditya.usage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.example.aditya.usage.Fragment.UsagePatternFragment;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;

public class AppUsagePatternActivity extends BaseActivity {

    private final String FRAG_TAG = "usage_pattern_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage_pattern);

        FragmentManager manager = getSupportFragmentManager();
        Intent i = getIntent();

        if(i.hasExtra(Constants.KEY_SHOW_FUTILE_APPS) &&
                i.getStringExtra(Constants.KEY_SHOW_FUTILE_APPS).equals(Constants.VALUE_SHOW_FUTILE_APPS)) {

            //initActionBar(CODE_BACK_ENABLED, "Unused Apps");

            UsagePatternFragment fragment = new UsagePatternFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_SHOW_FUTILE_APPS, Constants.VALUE_SHOW_FUTILE_APPS);
            fragment.setArguments(bundle);

            if(manager.findFragmentByTag(FRAG_TAG) == null) {
                manager.beginTransaction().add(android.R.id.content, fragment, FRAG_TAG).commit();
            }

        } else {

            //initActionBar(CODE_BACK_ENABLED, "Usage pattern");
            if(manager.findFragmentByTag(FRAG_TAG) == null) {
                manager.beginTransaction()
                        .add(android.R.id.content, new UsagePatternFragment(), FRAG_TAG).commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_usage_pattern, menu);
        return true;
    }
}
