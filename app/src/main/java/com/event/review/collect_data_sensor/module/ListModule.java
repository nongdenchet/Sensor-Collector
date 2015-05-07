package com.event.review.collect_data_sensor.module;

import com.event.review.collect_data_sensor.fragment.ListSensorDataFragment;
import com.event.review.collect_data_sensor.presenter.ListSensorDataPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nongdenchet on 5/4/15.
 */
@Module(injects = {
        ListSensorDataFragment.class,
}, complete = false)
public class ListModule {

    @Provides
    @Singleton
    public ListSensorDataPresenter provideListSensorPresenter() {
        return new ListSensorDataPresenter();
    }
}
