package com.event.review.collect_data_sensor.model;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.Gson;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nongdenchet on 5/3/15.
 */
public class SensorCollectionTest extends TestCase {
    private Gson gson;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gson = new Gson();
    }

    public void testParseJson() {
        SensorData data1 = new SensorData(1, 2, 3, 4, 5);
        SensorData data2 = new SensorData(123.123, 223, 0.3, -43, 1000);
        SensorData data3 = new SensorData(7.1, 200.12, 3.2, -0.77, 5000);
        List<SensorData> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);

        SensorCollection expect = new SensorCollection(
                dataList, 1000, "rv", SensorCollection.TRANSPORT.BIKE, 320, false);
        String json = gson.toJson(expect, SensorCollection.class);
        Log.e("Quan", json);
        SensorCollection actual = gson.fromJson(json, SensorCollection.class);

        assertThat(actual).isNotNull();
        SensorData[] test = new SensorData[3];
        assertThat(actual.getSensorDatas()).hasSize(3).contains(expect.getSensorDatas().toArray(test));
        assertThat(actual.getTimestamp()).isEqualTo(1000);
        assertThat(actual.getName()).isEqualTo("rv");
        assertThat(actual.getType()).isEqualTo(SensorCollection.TRANSPORT.BIKE);
        assertThat(actual.getDelta()).isEqualTo(320);
        assertThat(actual.isHasGravity()).isEqualTo(false);
    }

    public void testConvertToJson() {
        String json = "{\n" +
                "  \"name\": \"rv\",\n" +
                "  \"sensor_data\": [\n" +
                "    {\n" +
                "      \"accelerator\": 4,\n" +
                "      \"timestamp\": 5,\n" +
                "      \"x\": 1,\n" +
                "      \"y\": 2,\n" +
                "      \"z\": 3\n" +
                "    },\n" +
                "    {\n" +
                "      \"accelerator\": -43,\n" +
                "      \"timestamp\": 1000,\n" +
                "      \"x\": 123.123,\n" +
                "      \"y\": 223,\n" +
                "      \"z\": 0.3\n" +
                "    },\n" +
                "    {\n" +
                "      \"accelerator\": -0.77,\n" +
                "      \"timestamp\": 5000,\n" +
                "      \"x\": 7.1,\n" +
                "      \"y\": 200.12,\n" +
                "      \"z\": 3.2\n" +
                "    }\n" +
                "  ],\n" +
                "  \"type\": \"BIKE\",\n" +
                "  \"gravity\": false,\n" +
                "  \"delta\": 320,\n" +
                "  \"timestamp\": 1000\n" +
                "}";

        SensorData data1 = new SensorData(1, 2, 3, 4, 5);
        SensorData data2 = new SensorData(123.123, 223, 0.3, -43, 1000);
        SensorData data3 = new SensorData(7.1, 200.12, 3.2, -0.77, 5000);
        List<SensorData> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);

        SensorCollection expect = new SensorCollection(
                dataList, 1000, "rv", SensorCollection.TRANSPORT.BIKE, 320, false);
        SensorCollection actual = gson.fromJson(json, SensorCollection.class);

        assertThat(actual).isNotNull();
        SensorData[] test = new SensorData[3];
        assertThat(actual.getSensorDatas()).hasSize(3).contains(expect.getSensorDatas().toArray(test));
        assertThat(actual.getTimestamp()).isEqualTo(1000);
        assertThat(actual.getName()).isEqualTo("rv");
        assertThat(actual.getType()).isEqualTo(SensorCollection.TRANSPORT.BIKE);
        assertThat(actual.getDelta()).isEqualTo(320);
        assertThat(actual.isHasGravity()).isEqualTo(false);
    }

    public void testParcel() {
        SensorData data1 = new SensorData(1, 2, 3, 4, 5);
        SensorData data2 = new SensorData(123.123, 223, 0.3, -43, 1000);
        SensorData data3 = new SensorData(7.1, 200.12, 3.2, -0.77, 5000);
        List<SensorData> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);

        SensorCollection expect = new SensorCollection(
                dataList, 1000, "rv", SensorCollection.TRANSPORT.BIKE, 320, false);
        Parcel parcel = Parcel.obtain();
        expect.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        SensorCollection actual = SensorCollection.CREATOR.createFromParcel(parcel);

        assertThat(actual).isNotNull();
        SensorData[] test = new SensorData[3];
        assertThat(actual.getSensorDatas()).hasSize(3).contains(expect.getSensorDatas().toArray(test));
        assertThat(actual.getTimestamp()).isEqualTo(1000);
        assertThat(actual.getName()).isEqualTo("rv");
        assertThat(actual.getType()).isEqualTo(SensorCollection.TRANSPORT.BIKE);
        assertThat(actual.getDelta()).isEqualTo(320);
        assertThat(actual.isHasGravity()).isEqualTo(false);
    }
}