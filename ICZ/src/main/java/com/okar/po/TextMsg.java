package com.okar.entry;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangfengchen on 15/1/14.
 */
@DatabaseTable(tableName = "text_msg")
public class TextMsg implements Serializable {

    private static final long serialVersionUID = -5683263669918171030L;

    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    public String content;
    @DatabaseField(dataType = DataType.DATE)
    public Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TextMsg{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}