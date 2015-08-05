package com.example.aditya.usage.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by aditya on 09/07/15.
 */
public class BaseActivity extends ActionBarActivity {

    protected final int BACK_ALLOWED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void initToolbar(int code, String label) {

        getSupportActionBar().setTitle(label);
        if(BACK_ALLOWED == code) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
