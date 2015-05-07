package com.event.review.collect_data_sensor.presenter;

import android.graphics.Color;

import com.event.review.collect_data_sensor.model.SensorCollection;
import com.event.review.collect_data_sensor.model.SensorData;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nongdenchet on 5/4/15.
 */
public class CollectDataSensorPresenter extends BasePresenter {
    private List<Double> listX, listY, listZ, listA;
    private List<Long> listTime;
    private String mDescription = "";
    private ICollectDataSensor mView;

    public CollectDataSensorPresenter() {}

    public void setView(ICollectDataSensor view) {
        mView = view;
    }

    // Initalize data
    public void allocateData() {
        // allocations
        listX = new ArrayList<>();
        listY = new ArrayList<>();
        listZ = new ArrayList<>();
        listA = new ArrayList<>();
        listTime = new ArrayList<>();
    }

    // Create data for single line
    private LineDataSet create(int count, List data, int color, String name) {
        // add data for y-axis
        ArrayList<Entry> yVals = new ArrayList<>();
        if (count < 0 || count >= data.size())
            for (int i = 0; i < data.size(); i++) {
                String val = data.get(i).toString();
                yVals.add(new Entry(Float.valueOf(val), i));
            }
        else {
            int start = data.size() - count;
            for (int i = start; i < data.size(); i++) {
                float val = Float.valueOf(data.get(i).toString());
                yVals.add(new Entry(val, i - start));
            }
        }

        // create a dataset and give it a type
        LineDataSet ySet = new LineDataSet(yVals, name);
        ySet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ySet.setColor(color);
        ySet.setCircleColor(Color.WHITE);
        ySet.setLineWidth(2f);
        ySet.setCircleSize(3f);
        ySet.setFillAlpha(65);
        ySet.setFillColor(color);
        ySet.setHighLightColor(Color.rgb(244, 117, 117));
        ySet.setDrawCircleHole(false);

        return ySet;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setCollection(SensorCollection collection) {
        List<SensorData> datas = collection.getSensorDatas();
        for (int i = 0; i < datas.size(); i++) {
            updateUiCollector(datas.get(i), false);
        }
    }

    public void updateUiCollector(SensorData sensorData, boolean update) {
        // update data
        listX.add(sensorData.getX());
        listY.add(sensorData.getY());
        listZ.add(sensorData.getZ());
        listTime.add(sensorData.getTimestamp());
        listA.add(sensorData.getAccelerator());
        if (update) {
            updateChart();
        }
    }

    public void updateChart() {
        if (listTime != null) {
            // update ui here
            setData(listTime.size());
            mView.onChartUpdate();
        }
    }

    // Set data for graph
    public void setData(int count) {
        // add data for x-axis
        ArrayList<String> xVals = new ArrayList<>();
        if (count < 0 || count >= listTime.size())
            for (int i = 0; i < listTime.size(); i++) {
                xVals.add(listTime.get(i) + "");
            }
        else {
            for (int i = listTime.size() - count; i < listTime.size(); i++) {
                xVals.add(listTime.get(i) + "");
            }
        }

        // add all set of data
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(create(count, listA, ColorTemplate.getHoloBlue(), "Accelerator"));
        dataSets.add(create(count, listX, Color.RED, "X-accelerator"));
        dataSets.add(create(count, listY, Color.GREEN, "Y-accelerator"));
        dataSets.add(create(count, listZ, Color.YELLOW, "Z-accelerator"));

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        mView.onChartSetup(data, mDescription);
    }

    @Override
    public void init() {
        allocateData();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public interface ICollectDataSensor {
        public void onChartSetup(LineData data, String des);
        public void onChartUpdate();
    }
}
