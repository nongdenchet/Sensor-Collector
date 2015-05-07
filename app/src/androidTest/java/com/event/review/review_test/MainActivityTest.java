package com.event.review.review_test;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.test.ActivityUnitTestCase;
import android.view.View;

import com.event.review.R;

/**
 * Created by nongdenchet on 4/27/15.
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
    private Intent mServiceIntent;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public void testIfButtonHasClickListener() {
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        View testButton = getActivity().
                findViewById(R.id.background_job_btn);

        // Test if this button has the listener
        assertTrue("Button is missing onClick listener!",
                testButton.hasOnClickListeners());
    }

    public void testIfClickListenerStartsServiceCorrectly() {
        MyMockContext myMockContext = new MyMockContext(getInstrumentation().
                getTargetContext());
        setActivityContext(myMockContext);
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        View testButton = getActivity().
                findViewById(R.id.background_job_btn);

        // Test if the action send by intent is the same
        Intent backgroundJob = new Intent(MainActivity.ACTION_START_BACKGROUND_JOB);
        myMockContext.startService(backgroundJob);
        assertEquals("Wrong Intent action for starting service!",
                "startBackgroundJob", mServiceIntent.getAction());
    }

    public class MyMockContext extends ContextWrapper {
        public MyMockContext(Context base) {
            super(base);
        }

        @Override
        public ComponentName startService(Intent serviceIntent) {
            mServiceIntent = serviceIntent;
            return new ComponentName("com.event.review.review_test", "MyService");
        }
    }
}