package com.event.review.collect_data_sensor.presenter;

import android.content.Context;
import android.widget.Toast;

import com.event.review.R;
import com.event.review.collect_data_sensor.common.ObservableProperty;
import com.event.review.collect_data_sensor.fragment.CollectDataSensorFragment;
import com.event.review.collect_data_sensor.model.SensorCollection;
import com.event.review.collect_data_sensor.model.SensorData;
import com.event.review.collect_data_sensor.util.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by nongdenchet on 5/4/15.
 */
public class ListSensorDataPresenter extends BasePresenter {
    private StringBuffer buffer;
    private IListSensorData mView;

    // Observe sort type
    private ObservableProperty<SensorCollection.TRANSPORT> sortOrder
            = new ObservableProperty<>(SensorCollection.TRANSPORT.NONE);

    // Observe item sort title
    private ObservableProperty<Boolean> showSort
            = new ObservableProperty<>(true);

    // Data set
    private List<SensorCollection> collectionList;

    @Override
    public void init() {
        buffer = new StringBuffer();
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    public void setView(IListSensorData view) {
        this.mView = view;
    }

    public ObservableProperty<SensorCollection.TRANSPORT> getSortOrder() {
        return sortOrder;
    }

    public ObservableProperty<Boolean> getShowSort() {
        return showSort;
    }

    public void setSortOrder(SensorCollection.TRANSPORT value) {
        sortOrder.setValue(value);
    }

    public void setShowSort(boolean value) {
        showSort.setValue(value);
    }

    public CollectDataSensorFragment prepareFragment(int position) {
        SensorCollection collection = collectionList.get(position - 1);

        // Update action bar title
        String type = collection.getType().toString().toLowerCase();
        buffer.setLength(0);
        buffer.append(type);
        buffer.setCharAt(0, Character.toUpperCase(type.charAt(0)));
        mView.updateTitleActionbar(buffer.toString() + " - " + collection.getSensorDatas().size());

        // Add des
        String des = "number: " + collection.getSensorDatas().size()
                + ", delta: " + collection.getDelta()
                + ", gravity: " + collection.isHasGravity();

        // Add graph fragment
        return CollectDataSensorFragment
                .createWithDesAndCollection(R.layout.fragment_collect_data_sensor, collection, des);
    }

    public void startReadData(final Context context) {
        collectionList = new ArrayList<>();
        readDataTask(context).doOnTerminate(new Action0() {
            @Override
            public void call() {
                mView.doneLoadData();
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(context, "Error occurs!!!", Toast.LENGTH_SHORT)
                        .show();
            }
        }).doOnRequest(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                mView.startLoadData();
            }
        }).subscribe(new Action1<List<SensorCollection>>() {
            @Override
            public void call(List<SensorCollection> sensorCollections) {
                collectionList = sensorCollections;
                mView.successLoadData(sensorCollections);
            }
        });
    }

    // Observale task to read data
    public Observable<List<SensorCollection>> readDataTask(final Context context) {
        return Observable.create(new Observable.OnSubscribe<List<SensorCollection>>() {
            @Override
            public void call(Subscriber<? super List<SensorCollection>> subscriber) {
                subscriber.onNext(readData(context));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // Read all files data
    private List<SensorCollection> readData(Context context) {
        File[] allFiles = context.getFilesDir().listFiles();
        int length = allFiles.length;
        List<SensorCollection> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            SensorCollection data = readSensor(allFiles[i]);
            if (data != null) {
                list.add(data);
            }
        }
        return list;
    }

    // Read single collection
    private SensorCollection readSensor(File file) {
        SensorCollection collection = null;
        FileReader fileReader;
        BufferedReader reader;
        if (!file.isDirectory()) {
            try {
                fileReader = new FileReader(file);
                reader = new BufferedReader(fileReader);
                String line;

                // Reader header
                line = reader.readLine();
                if (line != null && line.equals(Constant.SENSOR_MARK)) {

                    String type = reader.readLine().toUpperCase();
                    int count = Integer.parseInt(reader.readLine());
                    long delta = Long.parseLong(reader.readLine());
                    boolean hasGravity = Boolean.parseBoolean(reader.readLine());
                    long timestamp = Long.parseLong(reader.readLine());
                    List<SensorData> datas = new ArrayList<>();
                    String name = file.getName().split("\\.")[0];

                    while ((line = reader.readLine()) != null) {
                        String[] result = line.split(";");
                        double x = Double.valueOf(result[0]);
                        double y = Double.valueOf(result[1]);
                        double z = Double.valueOf(result[2]);
                        double a = Double.valueOf(result[3]);
                        long t = Long.valueOf(result[4]);
                        datas.add(new SensorData(x, y, z, a, t));
                    }

                    collection = new SensorCollection(datas, timestamp, name,
                            SensorCollection.TRANSPORT.valueOf(type), delta, hasGravity);
                }

                // Close reader
                reader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return collection;
    }

    public interface IListSensorData {
        public void updateTitleActionbar(String title);
        public void successLoadData(List<SensorCollection> collection);
        public void doneLoadData();
        public void startLoadData();
    }
}
