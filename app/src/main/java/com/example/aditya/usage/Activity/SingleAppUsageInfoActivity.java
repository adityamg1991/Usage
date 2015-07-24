package com.example.aditya.usage.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aditya.usage.Fragment.SingleAppUsageInfoFragment;
import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;

public class SingleAppUsageInfoActivity extends BaseActivity {

    private final String FRAG_TAG = "frag_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_app_usage_info);

        String packageName = getIntent().getStringExtra(Constants.PCK_NAME);
        String appLabel = getIntent().getStringExtra(Constants.APP_LABEL);
        initActionBar(CODE_BACK_ENABLED, appLabel);

        FragmentManager manager = getSupportFragmentManager();
        if(null == manager.findFragmentByTag(FRAG_TAG)) {

            SingleAppUsageInfoFragment fragment = SingleAppUsageInfoFragment.newInstance(packageName);
            manager.beginTransaction().add(android.R.id.content, fragment, FRAG_TAG).commit();
        }
    }
}
