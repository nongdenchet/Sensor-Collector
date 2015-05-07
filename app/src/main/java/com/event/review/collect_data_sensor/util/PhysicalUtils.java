package com.event.review.collect_data_sensor.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class PhysicalUtils {
    public static void vibrate(Context context, long mili) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(mili);
    }
}
