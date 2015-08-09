package com.event.review.collect_data_sensor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nongdenchet on 5/4/15.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 2;

    public DbHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTables(database);
        createTriggersAndIndices(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                migrateToVersion2(db);
        }
    }

    private void createTriggersAndIndices(SQLiteDatabase database) {
        SqlUtils.execSql(database, DbTable.SENSOR_DATA.getPostCreateSql());
        SqlUtils.execSql(database, DbTable.SENSOR_COLLECTION.getPostCreateSql());
    }

    private void createTables(SQLiteDatabase database) {
        SqlUtils.execSql(database, DbTable.SENSOR_DATA.getCreateSql());
        SqlUtils.execSql(database, DbTable.SENSOR_COLLECTION.getCreateSql());
    }

    private void migrateToVersion2(SQLiteDatabase database) {
        // Add 'has_gravity' and 'delta' column into the 'sensor_collection' table
        SqlUtils.execSql(database, "ALTER TABLE " + DbTable.SENSOR_COLLECTION
                + " ADD COLUMN " + DbField.HAS_GRAVITY.getName() + " "
                + DbField.HAS_GRAVITY.getType());

        SqlUtils.execSql(database, "ALTER TABLE " + DbTable.SENSOR_COLLECTION
                + " ADD COLUMN " + DbField.DELTA.getName() + " "
                + DbField.DELTA.getType());
    }
}
