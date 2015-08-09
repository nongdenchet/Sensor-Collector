package com.event.review.collect_data_sensor.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

public class SqlUtils {

    private static final String TAG = "SqlUtils";

    public static void bind(SQLiteStatement statement, int bindArg, String value) {
        if (value == null) {
            statement.bindNull(bindArg);
        } else {
            statement.bindString(bindArg, value);
        }
    }

    public static void bind(SQLiteStatement statement, int bindArg, Integer value) {
        if (value == null) {
            statement.bindNull(bindArg);
        } else {
            statement.bindLong(bindArg, value);
        }
    }

    public static void bind(SQLiteStatement statement, int bindArg, Double value) {
        if (value == null) {
            statement.bindNull(bindArg);
        } else {
            statement.bindDouble(bindArg, value);
        }
    }

    public static void bind(SQLiteStatement statement, int bindArg, Long value) {
        if (value == null) {
            statement.bindNull(bindArg);
        } else {
            statement.bindLong(bindArg, value);
        }
    }

    public static void execSql(SQLiteDatabase db, String sql) {
        try {
            if (!TextUtils.isEmpty(sql)) {
                db.execSQL(sql);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error executing sql statement", e);
        }
    }

    public static void execSql(SQLiteDatabase db, String... sqlStatements) {
        if (sqlStatements != null) {
            for (String sql : sqlStatements) {
                if (!TextUtils.isEmpty(sql)) {
                    db.execSQL(sql);
                }
            }
        }
    }
}
