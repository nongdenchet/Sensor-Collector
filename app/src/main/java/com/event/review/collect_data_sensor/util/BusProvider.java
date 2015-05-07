package com.event.review.collect_data_sensor.util;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class BusProvider {

    private static MainThreadBus sBus;

    private BusProvider() {
        // Hide this constructor
    }

    public static MainThreadBus get() {
        if (sBus == null) {
            sBus = new MainThreadBus();
        }

        return sBus;
    }
}