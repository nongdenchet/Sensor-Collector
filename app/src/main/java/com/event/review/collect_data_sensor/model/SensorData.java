package com.event.review.collect_data_sensor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nongdenchet on 3/21/15.
 */
public class SensorData implements Parcelable {
    @SerializedName("x")
    private double x;

    @SerializedName("y")
    private double y;

    @SerializedName("z")
    private double z;

    @SerializedName("accelerator")
    private double accelerator;

    @SerializedName("timestamp")
    private long timestamp;

    public SensorData(double x, double y, double z,
                      double accelerator, long timestamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
        this.accelerator = accelerator;
    }

    public SensorData() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAccelerator() {
        return accelerator;
    }

    public void setAccelerator(double accelerator) {
        this.accelerator = accelerator;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SensorData> CREATOR = new Creator<SensorData>() {

        public SensorData createFromParcel(Parcel in) {
            SensorData data = new SensorData();
            data.x = in.readDouble();
            data.y = in.readDouble();
            data.z = in.readDouble();
            data.accelerator = in.readDouble();
            data.timestamp = in.readLong();
            return data;
        }

        public SensorData[] newArray(int size) {
            return new SensorData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x);
        dest.writeDouble(y);
        dest.writeDouble(z);
        dest.writeDouble(accelerator);
        dest.writeLong(timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SensorData))
            return false;
        SensorData data = (SensorData) o;
        return !(x != data.x || y != data.y || z != data.z
                || data.accelerator != accelerator || timestamp != data.timestamp);
    }
}
