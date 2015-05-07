package com.event.review.collect_data_sensor.interfaces;

/**
 * Created by nongdenchet on 4/29/15.
 */
public interface Injectable {
    Object[] getModules();
    void inject(Object o);
}
