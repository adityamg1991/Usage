package com.example.aditya.usage.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.aditya.usage.Activity.SingleAppUsageInfoActivity;
import com.example.aditya.usage.Utilities.Constants;

/**
 * Created by aditya on 27/07/15.
 */
public class UsageBaseFragment extends Fragment {

    void startSingleAppInfoActivity(String pck, String label) {

        Intent i = new Intent(getActivity(), SingleAppUsageInfoActivity.class);
        i.putExtra(Constants.PCK_NAME, pck);
        i.putExtra(Constants.APP_LABEL, label);
        startActivity(i);
    }
}
