package com.event.review.review_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.event.review.R;

public class MainActivity extends Activity {
    public static final String ACTION_START_BACKGROUND_JOB
            = "startBackgroundJob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBackgroundJob(View view) {
        Intent backgroundJob = new Intent(ACTION_START_BACKGROUND_JOB);
        startService(backgroundJob);
    }
}
