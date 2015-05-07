package com.event.review.collect_data_sensor.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.event.review.R;
import com.event.review.collect_data_sensor.application.SensorApp;
import com.event.review.collect_data_sensor.fragment.CollectDataSensorFragment;
import com.event.review.collect_data_sensor.fragment.SettingFragment;
import com.event.review.collect_data_sensor.model.SensorData;
import com.event.review.collect_data_sensor.common.Navigator;
import com.event.review.collect_data_sensor.service.CollectDataSensorService;
import com.event.review.collect_data_sensor.util.PrefUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by nongdenchet on 3/21/15.
 */
public class CollectDataSensorActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private MenuItem actionItem;
    private boolean start;
    private CollectDataSensorFragment mCollectFragment;
    private SettingFragment mSettingFragment;

    @Inject Bus mEventBus;
    @Inject Navigator mNavigator;
    @Inject PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_data_activity);

        // Init UI
        mCollectFragment = CollectDataSensorFragment.create(R.layout.fragment_collect_data_sensor);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mCollectFragment)
                .commit();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        actionBar = getSupportActionBar();

        // Setup injects
        SensorApp.get(this)
                .getObjectGraph()
                .inject(this);

        // Regain state
        start = prefUtils.get("collecting", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // setup actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.show();

        // setup menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        actionItem = menu.findItem(R.id.action);
        if (start) {
            actionItem.setTitle(getString(R.string.stop));
        } else {
            actionItem.setTitle(getString(R.string.start));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_view:
                mNavigator.startReviewActivity(this);
                break;
            case R.id.action:
                if (start) {
                    stopCollect();
                } else {
                    startCollect();
                }
                start = !start;
                break;
            case R.id.settings:
                mNavigator.startSettingActivity(this);
                break;
        }
        return true;
    }

    // Start the service
    public void startCollect() {
        new MaterialDialog.Builder(this)
                .title("Choose your transporting")
                .items(R.array.tranport_items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        actionItem.setTitle(getString(R.string.stop));
                        mCollectFragment.getPresenter().allocateData();
                        mNavigator.startCollectDataSensorService(CollectDataSensorActivity.this, text.toString());
                        return true;
                    }
                })
                .cancelable(true)
                .negativeText(getString(R.string.cancel))
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                })
                .show();
    }

    // Stop the service
    public void stopCollect() {
        // stop process and save data
        actionItem.setTitle(getString(R.string.start));
        mNavigator.stopCollectDataSensorService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
        actionBar.setTitle(getString(R.string.graph_analyzer));
        start = prefUtils.get("collecting", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(CollectDataSensorService.StopEvent event) {
        actionItem.setTitle(getString(R.string.start));
    }

    @Subscribe
    public void onEvent(SensorData sensorData) {
        mCollectFragment.getPresenter().updateUiCollector(sensorData, true);
    }
}
