package com.okar.po;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by wangfengchen on 15/1/14.
 */

public class TextMsg implements Serializable {

    private static final long serialVersionUID = -5683263669918171030L;

    @DatabaseField(id=true)
    int id;
    @DatabaseField
    public String text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}