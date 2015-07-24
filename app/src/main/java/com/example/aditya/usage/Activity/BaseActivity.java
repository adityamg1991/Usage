package com.example.aditya.usage.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.aditya.usage.R;
import com.example.aditya.usage.Utilities.Constants;

/**
 * Created by aditya on 09/07/15.
 */
public class BaseActivity extends ActionBarActivity {

    protected final int CODE_BACK_ENABLED = 1;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.APP_THEME_HASH_CODE)));
    }


    protected void initActionBar(int code, String title) {

        actionBar.setTitle(title);

        if(CODE_BACK_ENABLED == code) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
