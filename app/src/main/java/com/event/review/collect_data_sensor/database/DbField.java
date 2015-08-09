package com.event.review.collect_data_sensor.database;

/**
 * Created by nongdenchet on 5/4/15.
 */
public class DbField extends DatabaseField {
    public DbField(String name, String type) {
        super(name, type);
    }

    public DbField(String name, String type, String constraint) {
        super(name, type, constraint);
    }

    public static final DbField AUTO_ID = new DbField("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT");
    public static final DbField PARENT_ID = new DbField("parent_id", "text");
    public static final DbField X = new DbField("x", "real");
    public static final DbField Y = new DbField("y", "real");
    public static final DbField Z = new DbField("z", "real");
    public static final DbField A = new DbField("accelerator", "real");
    public static final DbField TIME_STAMP = new DbField("timestamp", "integer");
    public static final DbField TYPE = new DbField("type", "text");
    public static final DbField DELTA = new DbField("delta", "integer");
    public static final DbField NAME = new DbField("name", "text");
    public static final DbField HAS_GRAVITY = new DbField("has_gravity", "integer");
}
