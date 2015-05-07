package com.event.review.collect_data_sensor.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.event.review.R;
import com.event.review.collect_data_sensor.application.SensorApp;
import com.event.review.collect_data_sensor.fragment.ListSensorDataFragment;
import com.event.review.collect_data_sensor.model.SensorCollection;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by nongdenchet on 3/22/15.
 */
public class SensorDataActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private MenuItem actionItem;
    private ListSensorDataFragment mListFragment;

    @Inject Bus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_data_activity);
        actionBar = getSupportActionBar();

        // Setup injects
        SensorApp.get(this)
                .getObjectGraph()
                .inject(this);
        ButterKnife.inject(this);

        // Add list fragment
        mListFragment = new ListSensorDataFragment(this);
        mListFragment.setLayout(R.layout.fragment_list);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mListFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.show();

        // Setup menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review_menu, menu);
        actionItem = menu.findItem(R.id.sortMenuItem);

        // Observer title
        mListFragment.getPresenter().getShowSort()
                .observe()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean value) {
                        actionItem.setVisible(value);
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Delete":
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mListFragment.removeCollection(info.position);
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.walkMenuItem:
                mListFragment.getPresenter().setSortOrder(SensorCollection.TRANSPORT.WALK);
                break;
            case R.id.bikeMenuItem:
                mListFragment.getPresenter().setSortOrder(SensorCollection.TRANSPORT.BIKE);
                break;
            case R.id.busMenuItem:
                mListFragment.getPresenter().setSortOrder(SensorCollection.TRANSPORT.BUS);
                break;
            case R.id.motorMenuItem:
                mListFragment.getPresenter().setSortOrder(SensorCollection.TRANSPORT.MOTORBIKE);
                break;
            case R.id.carMenuItem:
                mListFragment.getPresenter().setSortOrder(SensorCollection.TRANSPORT.CAR);
                break;
            case R.id.none:
                mListFragment.getPresenter().setSortOrder(SensorCollection.TRANSPORT.NONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getString(R.string.app_name));
        mEventBus.register(this);
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

}
