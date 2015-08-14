package com.coffeemug.usage.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.coffeemug.usage.Fragment.SingleAppUsageInfoFragment;
import com.coffeemug.usage.Utilities.Constants;
import com.coffeemug.usage.R;

public class SingleAppUsageInfoActivity extends ToolBarBaseActivity {

    private final String FRAG_TAG = "frag_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_app_usage_info);

        String packageName = getIntent().getStringExtra(Constants.PCK_NAME);
        String appLabel = getIntent().getStringExtra(Constants.APP_LABEL);

        initialiseToolbar(appLabel);

        FragmentManager manager = getSupportFragmentManager();
        if(null == manager.findFragmentByTag(FRAG_TAG)) {

            SingleAppUsageInfoFragment fragment = SingleAppUsageInfoFragment.newInstance(packageName);
            manager.beginTransaction().add(R.id.ll_container, fragment, FRAG_TAG).commit();
        }
    }
}
