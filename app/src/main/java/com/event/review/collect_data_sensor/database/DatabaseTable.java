package com.event.review.collect_data_sensor.database;

public class DatabaseTable {

    private String mName;
    private DatabaseField[] mFields;
    private String[] mCustomScripts;
    private String[] mFieldNames;

    public DatabaseTable(String name, DatabaseField[] fields, String... customScripts) {
        mName = name;
        mFields = fields;
        mCustomScripts = customScripts;
    }

    public String getName() {
        return mName;
    }

    public DatabaseField[] getFields() {
        return mFields;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String[] getFieldNames() {
        if (mFieldNames == null) {
            mFieldNames = new String[mFields.length];

            for (int i = 0, size = mFields.length; i < size; i++) {
                mFieldNames[i] = mFields[i].getName();
            }
        }

        return mFieldNames;
    }

    public String getDropSql() {
        return "DROP TABLE IF EXISTS " + mName;
    }

    public String[] getPostCreateSql() {
        return mCustomScripts;
    }

    public String getCreateSql() {
        StringBuilder sqlTextBuilder = new StringBuilder()
                .append("CREATE TABLE ").append(mName).append(" ").append("(");

        // Ensure that a comma does not appear on the last iteration
        String comma = "";
        DatabaseField[] fields = getFields();
        for (DatabaseField field : fields) {
            sqlTextBuilder.append(comma);
            comma = ",";

            sqlTextBuilder.append(field.getName());
            sqlTextBuilder.append(" ");
            sqlTextBuilder.append(field.getType());
            sqlTextBuilder.append(" ");

            if (field.getConstraint() != null) {
                sqlTextBuilder.append(field.getConstraint());
            }
        }

        sqlTextBuilder.append(")");
        return sqlTextBuilder.toString();
    }
}
