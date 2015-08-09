package com.event.review.collect_data_sensor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.review.R;
import com.event.review.collect_data_sensor.adapter.SensorDataAdapter;
import com.event.review.collect_data_sensor.application.SensorApp;
import com.event.review.collect_data_sensor.model.SensorCollection;
import com.event.review.collect_data_sensor.module.ListModule;
import com.event.review.collect_data_sensor.presenter.ListSensorDataPresenter;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnItemClick;
import rx.functions.Action1;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class ListSensorDataFragment extends BaseFragment implements ListSensorDataPresenter.IListSensorData {
    private SensorDataAdapter mAdapter;
    private ProgressDialog progressDialog;
    private CollectDataSensorFragment graphFragment;
    private Context mContext;
    private ActionBar mActionBar;
    private boolean hide = false;

    @InjectView(R.id.listView) ListView listView;
    @InjectView(R.id.nothing) TextView nothingText;
    @Inject Bus mEventBus;
    @Inject ListSensorDataPresenter mPresenter;

    public ListSensorDataFragment(Context context) {
        mContext = context;
    }
    public ListSensorDataFragment() {}

    public ListSensorDataPresenter getPresenter() {
        return mPresenter;
    }

    @OnItemClick(R.id.listView)
    void onItemClick(int position) {
        if (position != 0) {
            graphFragment = mPresenter.prepareFragment(position);

            // Transaction
            mPresenter.setShowSort(false);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, graphFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SensorApp.get(getActivity())
                .getObjectGraph()
                .plus(new ListModule())
                .inject(this);

        mPresenter.setView(this);
        mPresenter.init();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());

        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (hide) {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .show(ListSensorDataFragment.this)
                                    .commit();
                            hide = false;
                        } else {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .hide(ListSensorDataFragment.this)
                                    .commit();
                            hide = true;
                        }
                    }
                });

        // Read data
        mPresenter.startReadData(getActivity());

        // Init actionbar
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // Create context menu
        getActivity().registerForContextMenu(listView);
    }

    public void removeCollection(int pos) {
        if (mAdapter != null)
            mAdapter.removeCollection(pos - 1);
    }

    @Subscribe
    public void onEvent(SensorCollection collection) {
        if (mAdapter != null) {
            mAdapter.addCollection(collection);
            mAdapter.filterCollections(mPresenter.getSortOrder().getValue());
            updateNothing();
            Toast.makeText(mContext, "Update", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
        mEventBus.register(this);
        mPresenter.setShowSort(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
        mEventBus.unregister(this);
    }

    private void updateNothing() {
        if (mAdapter != null && mAdapter.getCollections().size() == 0) {
            nothingText.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            nothingText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateTitleActionbar(String title) {
        mActionBar.setTitle(title);
    }

    @Override
    public void successLoadData(List<SensorCollection> collectionList) {
        mAdapter = new SensorDataAdapter(mContext, collectionList);
        updateNothing();

        // Start observing
        mPresenter.getSortOrder().observe().subscribe(new Action1<SensorCollection.TRANSPORT>() {
            @Override
            public void call(SensorCollection.TRANSPORT transport) {
                mAdapter.filterCollections(transport);
                updateNothing();
            }
        });

        // Add adapter
        listView.setAdapter(mAdapter);
    }

    @Override
    public void doneLoadData() {
        progressDialog.dismiss();
    }

    @Override
    public void startLoadData() {
        progressDialog.setMessage("Reading data...");
        progressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
