package com.event.review.collect_data_sensor.module;

import android.content.Context;

import com.event.review.collect_data_sensor.activity.CollectDataSensorActivity;
import com.event.review.collect_data_sensor.activity.SensorDataActivity;
import com.event.review.collect_data_sensor.common.Navigator;
import com.event.review.collect_data_sensor.service.CollectDataSensorService;
import com.event.review.collect_data_sensor.util.BusProvider;
import com.event.review.collect_data_sensor.util.PrefUtils;
import com.google.gson.Gson;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nongdenchet on 4/29/15.
 */
@Module(injects = {
        CollectDataSensorActivity.class,
        CollectDataSensorService.class,
        SensorDataActivity.class
}, library = true)
public class AppModule {
    Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    public Bus provideEventBus() {
        return BusProvider.get();
    }

    @Provides
    @Singleton
    public Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    @Singleton
    public PrefUtils providePreUtils() {
        return new PrefUtils(mContext);
    }

    @Provides
    public StringBuffer provideStringBuffer() {
        return new StringBuffer();
    }

    @Provides
    public Gson provideGson() {
        return new Gson();
    }

}
