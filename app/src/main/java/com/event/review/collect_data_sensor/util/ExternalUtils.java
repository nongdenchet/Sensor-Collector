package com.event.review.collect_data_sensor.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by nongdenchet on 3/21/15.
 */
public class ExternalUtils {
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static boolean removeFile(Context context, String name) {
        File file = new File(context.getFilesDir(), name + ".txt");
        return file.delete();
    }
}
