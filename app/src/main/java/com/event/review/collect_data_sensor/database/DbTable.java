package com.event.review.collect_data_sensor.database;

/**
 * Created by nongdenchet on 5/4/15.
 */
public class DbTable extends DatabaseTable {
    public DbTable(String name, DatabaseField[] fields, String... customScripts) {
        super(name, fields, customScripts);
    }

    public static final DbTable SENSOR_DATA = new DbTable("semsor_data",
            new DbField[] {
                    DbField.AUTO_ID,
                    DbField.PARENT_ID,
                    DbField.X,
                    DbField.Y,
                    DbField.Z,
                    DbField.A,
                    DbField.TIME_STAMP,
            },
            "CREATE UNIQUE INDEX 'unique_code' ON scheduled_stops(" + DbField.AUTO_ID + ");"
    );

    public static final DbTable SENSOR_COLLECTION = new DbTable("semsor_collection",
            new DbField[] {
                    DbField.AUTO_ID,
                    DbField.TIME_STAMP,
                    DbField.NAME,
                    DbField.TYPE,
                    DbField.DELTA,
                    DbField.HAS_GRAVITY
            },
            "CREATE UNIQUE INDEX 'unique_code' ON scheduled_stops(" + DbField.AUTO_ID + ");"
    );
}
