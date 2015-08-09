package com.event.review.collect_data_sensor.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.event.review.R;
import com.event.review.collect_data_sensor.application.SensorApp;
import com.event.review.collect_data_sensor.model.SensorCollection;
import com.event.review.collect_data_sensor.module.CollectModule;
import com.event.review.collect_data_sensor.presenter.CollectDataSensorPresenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.InjectView;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class CollectDataSensorFragment extends BaseFragment
        implements CollectDataSensorPresenter.ICollectDataSensor, OnChartGestureListener, OnChartValueSelectedListener {

    private Context mContext;

    @Inject Bus mEventBus;
    @Inject CollectDataSensorPresenter mPresenter;
    @InjectView(R.id.chart) LineChart mChart;

    public CollectDataSensorFragment() {}

    public static CollectDataSensorFragment createWithDesAndCollection
            (int layout, SensorCollection collection, String des) {
        CollectDataSensorFragment mFragment = new CollectDataSensorFragment();
        mFragment.setLayout(layout);
        Bundle bundle = new Bundle();
        bundle.putParcelable("collection", collection);
        bundle.putString("description", des);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    public CollectDataSensorPresenter getPresenter() {
        return mPresenter;
    }

    public static CollectDataSensorFragment create(int layout) {
        CollectDataSensorFragment mFragment = new CollectDataSensorFragment();
        mFragment.setLayout(layout);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Setup injects
        SensorApp.get(getActivity())
                .getObjectGraph()
                .plus(new CollectModule())
                .inject(this);

        // Init
        mContext = getActivity();
        mPresenter.setView(this);
        mPresenter.init();

        if (getArguments() != null) {
            Bundle extra = getArguments();
            mPresenter.setCollection(extra.<SensorCollection>getParcelable("collection"));
            mPresenter.setDescription(extra.getString("description"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
        mPresenter.pause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Ui
        mPresenter.updateChart();
        setUpUi();
    }

    public void setUpUi() {
        // set up the line chart
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x-axis and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        // add data
        // setData(listTime.size());
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(30);
        leftAxis.setAxisMinValue(-30);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChartSetup(LineData data, String des) {
        mChart.setDescription(des);
        mChart.setData(data);
    }

    @Override
    public void onChartUpdate() {
        mChart.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
