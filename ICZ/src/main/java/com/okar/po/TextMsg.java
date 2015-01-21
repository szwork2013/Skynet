package com.okar.po;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangfengchen on 15/1/14.
 */
@DatabaseTable(tableName = "text_msg")
public class TextMsg implements Serializable {

    private static final long serialVersionUID = -5683263669918171030L;

    @DatabaseField(id=true)
    int id;
    @DatabaseField
    public String content;
    @DatabaseField
    public String time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}