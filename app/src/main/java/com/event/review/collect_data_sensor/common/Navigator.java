package com.event.review.collect_data_sensor.common;

import android.content.Context;
import android.content.Intent;

import com.event.review.collect_data_sensor.activity.SensorDataActivity;
import com.event.review.collect_data_sensor.activity.SettingActivity;
import com.event.review.collect_data_sensor.service.CollectDataSensorService;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class Navigator {
    public Navigator() {
    }

    // Start a recording data service
    public void startCollectDataSensorService(Context context, String type) {
        Intent intentService = new Intent(context, CollectDataSensorService.class);
        intentService.putExtra("type", type);
        context.startService(intentService);
    }

    // Stop a recording data service
    public void stopCollectDataSensorService(Context context) {
        Intent intentService = new Intent(context, CollectDataSensorService.class);
        context.stopService(intentService);
    }

    // Start activity view
    public void startReviewActivity(Context context) {
        Intent intent = new Intent(context, SensorDataActivity.class);
        context.startActivity(intent);
    }

    // Start setting activity
    public void startSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
