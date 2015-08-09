package com.event.review.collect_data_sensor.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.event.review.R;
import com.event.review.collect_data_sensor.activity.CollectDataSensorActivity;
import com.event.review.collect_data_sensor.application.SensorApp;
import com.event.review.collect_data_sensor.model.SensorCollection;
import com.event.review.collect_data_sensor.model.SensorData;
import com.event.review.collect_data_sensor.util.Constant;
import com.event.review.collect_data_sensor.util.DateUtils;
import com.event.review.collect_data_sensor.util.PhysicalUtils;
import com.event.review.collect_data_sensor.util.PrefUtils;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by nongdenchet on 3/21/15.
 */
public class CollectDataSensorService extends Service implements SensorEventListener {
    private static final String TAG = "Service";
    private long lastUpdate = 0;
    private long firstTime;
    private int mDataCount, mRecordCount, mDelta;
    private boolean hasGravity;
    private boolean isStart = false;

    private final int ACCELERATION_THRESHOLD = 12;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private List<Double> listX, listY, listZ, listA;
    private List<Long> listTime;
    private String type;

    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    @Inject
    Bus mEventBus;
    @Inject
    PrefUtils prefUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create wakelock
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wake-lock");

        // Setup injects
        SensorApp.get(this)
                .getObjectGraph()
                .inject(this);

        allocateData();
        setUpSetting();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (hasGravity) {
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }
    }

    private void setUpSetting() {
        mDataCount = Integer.parseInt(prefUtils.get(getString(R.string.key_data), "16"));
        mRecordCount = Integer.parseInt(prefUtils.get(getString(R.string.key_record), "100"));
        mDelta = Integer.parseInt(prefUtils.get(getString(R.string.key_delta), "320"));
        hasGravity = !prefUtils.get(getString(R.string.key_gravity), false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        type = intent.getStringExtra("type");
        startRecording();
        startForeground(startId, getNotification());
        return Service.START_NOT_STICKY;
    }

    private Notification getNotification() {
        Intent i = new Intent(this, CollectDataSensorActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("Collecting Data")
                .setContentText("Click here to show the collecting process")
                .setContentIntent(pendIntent)
                .setSmallIcon(R.mipmap.ic_launcher);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    private void allocateData() {
        // allocations
        listX = new ArrayList<>();
        listY = new ArrayList<>();
        listZ = new ArrayList<>();
        listA = new ArrayList<>();
        listTime = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    public void startRecording() {
        if (!isStart) {
            wakeLock.acquire();
            senSensorManager.registerListener(this, senAccelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
            isStart = true;
            prefUtils.set("collecting", isStart);
        }
    }

    public void stopRecording() {
        if (isStart) {
            wakeLock.release();
            senSensorManager.unregisterListener(this);
            isStart = false;
            prefUtils.set("collecting", isStart);
            mEventBus.post(new StopEvent());
            stopSelf();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        int sensorType = sensor.getType();
        if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION
                || sensorType == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > mDelta) {
                lastUpdate = curTime;
                double accelerator = Math.sqrt(x * x + y * y + z * z);
                SensorData data = new SensorData(x, y, z, accelerator, curTime - firstTime);
                updateUiCollector(data);
                mEventBus.post(data);
            }
        }
    }

    // Detect change
    private boolean detectShake(double accelerator) {
        // Detect shake
        if (accelerator > ACCELERATION_THRESHOLD) {
            PhysicalUtils.vibrate(this, 500);
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateUiCollector(SensorData sensorData) {
        // update data
        listX.add(sensorData.getX());
        listY.add(sensorData.getY());
        listZ.add(sensorData.getZ());
        listTime.add(sensorData.getTimestamp());
        listA.add(sensorData.getAccelerator());

        // store data
        if (listA.size() == mRecordCount) {
            writeToFilesTask().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            // Save battery
                            if (mDataCount == 0) {
                                Log.e(TAG, "Stopped");
                                stopRecording();
                            }
                        }
                    })
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean value) {
                            if (value) {
                                allocateData();
                                mDataCount--;
                            } else {
                                Log.e(TAG, "Fail to save file");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Log.e(TAG, "Problem writing files");
                        }
                    });
        }
    }

    public Observable<Boolean> writeToFilesTask() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Boolean res = writeFiles();
                    subscriber.onNext(res);
                    subscriber.onCompleted();
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopRecording();
        super.onTaskRemoved(rootIntent);
    }

    private void notifyEvent(long mili, String name, int size) {
        ArrayList<SensorData> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new SensorData(listX.get(i), listY.get(i),
                    listZ.get(i), listA.get(i), listTime.get(i)));
        }
        SensorCollection collection =
                new SensorCollection(list, mili, name,
                        SensorCollection.TRANSPORT.valueOf(type.toUpperCase())
                        , mDelta, hasGravity);
        mEventBus.post(collection);
    }

    private boolean writeFiles() throws IOException {
        long current = System.currentTimeMillis();
        String name = "SENSOR-DATA "
                + DateUtils.getCurrentDate(current);
        File file = new File(getFilesDir(), name + ".txt");
        FileWriter writer;
        try {
            // create new writer
            writer = new FileWriter(file);

            // write the header
            writer.write(Constant.SENSOR_MARK);
            writer.write("\n");

            // write the type
            writer.write(type);
            writer.write("\n");

            // write number of record
            writer.write(String.valueOf(listA.size()));
            writer.write("\n");

            // write delta
            writer.write(String.valueOf(mDelta));
            writer.write("\n");

            // write has gravity
            writer.write(String.valueOf(hasGravity));
            writer.write("\n");

            // write timestamp
            writer.write(String.valueOf(current));
            writer.write("\n");

            // write the data
            for (int i = 0; i < listTime.size(); i++) {
                writer.write(listX.get(i) + ";" + listY.get(i) + ";" + listZ.get(i)
                        + ";" + listA.get(i) + ";" + listTime.get(i));
                writer.write("\n");
            }
            Log.e(TAG, "File has been saved at: " + file.getAbsolutePath());
            writer.close();
        } finally {
            // Notify
            notifyEvent(current, file.getName(), listTime.size());
        }
        return true;
    }

    public class StopEvent {
    }
}
