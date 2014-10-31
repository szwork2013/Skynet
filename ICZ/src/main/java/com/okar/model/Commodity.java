package com.okar.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangfengchen on 14/10/31.
 */
@DatabaseTable(tableName = "commodity")
public class Commodity {
    @DatabaseField(generatedId = true)
    public int id;
    public String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
