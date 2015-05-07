package com.event.review.collect_data_sensor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nongdenchet on 3/22/15.
 */
public class SensorCollection implements Parcelable {
    @SerializedName("sensor_data")
    private List<SensorData> sensorDatas;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private TRANSPORT type;

    @SerializedName("delta")
    private long delta;

    @SerializedName("gravity")
    private boolean hasGravity;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SensorCollection> CREATOR = new Creator<SensorCollection>() {

        public SensorCollection createFromParcel(Parcel in) {
            SensorCollection data = new SensorCollection();
            data.timestamp = in.readLong();
            data.name = in.readString();
            data.type = TRANSPORT.valueOf(in.readString());
            data.delta = in.readLong();
            data.hasGravity = Boolean.parseBoolean(in.readString());
            data.sensorDatas = new ArrayList<>();
            in.readTypedList(data.sensorDatas, SensorData.CREATOR);
            return data;
        }

        public SensorCollection[] newArray(int size) {
            return new SensorCollection[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeString(name);
        dest.writeString(type.toString());
        dest.writeLong(delta);
        dest.writeString(String.valueOf(hasGravity));
        dest.writeTypedList(sensorDatas);
    }

    public enum TRANSPORT {
        WALK,
        BIKE,
        BUS,
        CAR,
        MOTORBIKE,
        NONE
    }

    public SensorCollection() {
    }

    public SensorCollection(List<SensorData> sensorDatas, long timestamp,
                            String name, TRANSPORT type, long delta, boolean hasGravity) {
        this.sensorDatas = sensorDatas;
        this.timestamp = timestamp;
        this.name = name;
        this.type = type;
        this.delta = delta;
        this.hasGravity = hasGravity;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public boolean isHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public TRANSPORT getType() {
        return type;
    }

    public void setType(TRANSPORT type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SensorData> getSensorDatas() {
        return sensorDatas;
    }

    public void setSensorDatas(List<SensorData> sensorDatas) {
        this.sensorDatas = sensorDatas;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
