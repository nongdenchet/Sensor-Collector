package com.event.review.collect_data_sensor.module;

import com.event.review.collect_data_sensor.fragment.CollectDataSensorFragment;
import com.event.review.collect_data_sensor.presenter.CollectDataSensorPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nongdenchet on 5/4/15.
 */
@Module(injects = {
        CollectDataSensorFragment.class
}, complete = false)
public class CollectModule {

    @Provides
    @Singleton
    public CollectDataSensorPresenter provideDataSensorPresenter() {
        return new CollectDataSensorPresenter();
    }
}
