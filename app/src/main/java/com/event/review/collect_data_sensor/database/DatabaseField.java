package com.event.review.collect_data_sensor.database;

public class DatabaseField {

    private String mName;
    private String mType;
    private String mConstraint;

    public DatabaseField(String name, String type) {
        this(name, type, null);
    }

    public DatabaseField(String name, String type, String constraint) {
        mName = name;
        mType = type;
        mConstraint = constraint;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getConstraint() {
        return mConstraint;
    }
}