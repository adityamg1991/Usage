package com.coffeemug.usage.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.coffeemug.usage.Fragment.MemoryUsageFragment;
import com.coffeemug.usage.R;

public class MemoryUsageActivity extends ToolBarBaseActivity {

    private final String FRAG_TAG = "memory_usage_frag_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_usage);

        FragmentManager managerFragment = getSupportFragmentManager();
        if(managerFragment.findFragmentByTag(FRAG_TAG) == null) {
            managerFragment.beginTransaction().
                    add(R.id.rl_container, new MemoryUsageFragment(), FRAG_TAG).commit();
        }

        initialiseToolbar("Memory Usage");
    }
}
