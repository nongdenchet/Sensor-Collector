package com.event.review.collect_data_sensor.model;

import android.os.Parcel;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nongdenchet on 5/3/15.
 */
public class SensorDataTest extends AndroidTestCase {
    private Gson gson;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gson = new Gson();
    }

    public void testParseJson() {
        SensorData expect = new SensorData(1, 2, 3, 4, 5);
        String json = "{\n" +
                "  \"x\":\"1\",\n" +
                "  \"y\":\"2\",\n" +
                "  \"z\":\"3\",\n" +
                "  \"accelerator\":\"4\",\n" +
                "  \"timestamp\":\"5\"\n" +
                "}";
        SensorData actual = gson.fromJson(json, SensorData.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getAccelerator()).isEqualTo(expect.getAccelerator());
        assertThat(actual.getX()).isEqualTo(expect.getX());
        assertThat(actual.getY()).isEqualTo(expect.getY());
        assertThat(actual.getZ()).isEqualTo(expect.getZ());
        assertThat(actual.getTimestamp()).isEqualTo(expect.getTimestamp());
    }

    public void testConvertToJson() {
        SensorData expect = new SensorData(1, 2, 3, 4, 5);
        String json = gson.toJson(expect, SensorData.class);
        SensorData actual = gson.fromJson(json, SensorData.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getAccelerator()).isEqualTo(expect.getAccelerator());
        assertThat(actual.getX()).isEqualTo(expect.getX());
        assertThat(actual.getY()).isEqualTo(expect.getY());
        assertThat(actual.getZ()).isEqualTo(expect.getZ());
        assertThat(actual.getTimestamp()).isEqualTo(expect.getTimestamp());
    }

    public void testParcel() {
        SensorData expect = new SensorData(1, 2, 3, 4, 5);
        Parcel in = Parcel.obtain();
        expect.writeToParcel(in, 0);
        in.setDataPosition(0);
        SensorData actual = SensorData.CREATOR.createFromParcel(in);

        assertThat(actual).isNotNull();
        assertThat(actual.getAccelerator()).isEqualTo(expect.getAccelerator());
        assertThat(actual.getX()).isEqualTo(expect.getX());
        assertThat(actual.getY()).isEqualTo(expect.getY());
        assertThat(actual.getZ()).isEqualTo(expect.getZ());
        assertThat(actual.getTimestamp()).isEqualTo(expect.getTimestamp());
    }
}