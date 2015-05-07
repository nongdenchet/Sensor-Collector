package com.event.review.collect_data_sensor.application;

import android.app.Application;
import android.content.Context;

import com.event.review.collect_data_sensor.interfaces.Injectable;
import com.event.review.collect_data_sensor.module.AppModule;

import dagger.ObjectGraph;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class SensorApp extends Application implements Injectable {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private ObjectGraph objectGraph;

    public static SensorApp get(Context context) {
        return (SensorApp) context.getApplicationContext();
    }

    public ObjectGraph getObjectGraph() {
        if (objectGraph == null) {
            objectGraph = ObjectGraph.create(getModules());
        }

        return objectGraph;
    }

    /**
     * Use this interface function so that in test case, we can easily supply other modules.
     */
    @Override
    public Object[] getModules() {
        return new Object[]{
                new AppModule(this),
        };
    }

    public void inject(Object object) {
        getObjectGraph().inject(object);
    }
}
