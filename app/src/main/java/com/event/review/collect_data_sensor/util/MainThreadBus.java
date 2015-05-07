package com.event.review.collect_data_sensor.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * A custom {@link com.squareup.otto.Bus} that posts events from any thread and
 * lets subscribers receive them on the main thread
 */
public class MainThreadBus extends Bus {

    /**
     * A Handler used to communicate with the main thread
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public MainThreadBus() {
        super(ThreadEnforcer.ANY);
    }

    /**
     * Posts an event and expects to handle it on the main thread
     *
     * @param event The event that we want to post
     */
    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // We're on the main thread
            super.post(event);
        } else {
            // The operation inside run() will be called on the main thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }

    /**
     * NOTE: Use this method if we expect to handle the event on the same thread that has posted the event
     *
     * @param event The event that we want to post
     */
    public void postAsync(Object event) {
        super.post(event);
    }
}