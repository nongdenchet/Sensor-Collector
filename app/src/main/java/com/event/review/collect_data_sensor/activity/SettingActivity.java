package com.event.review.collect_data_sensor.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.event.review.R;
import com.event.review.collect_data_sensor.fragment.SettingFragment;

/**
 * Created by nongdenchet on 4/30/15.
 */
public class SettingActivity extends ActionBarActivity {
    private SettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_data_activity);
        mSettingFragment = new SettingFragment();
        getSupportActionBar().setTitle("Setting");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mSettingFragment)
                .commit();
    }

}
