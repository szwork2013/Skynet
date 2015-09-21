package com.okar.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by wangfengchen on 15/1/20.
 */
@DatabaseTable
public class MsgBody implements Parcelable{

    public final static String CHAT_TYPE = "chat";

    public final static String TEXT_MESSAGE_TYPE = "text";

    public final static int ME = 1;

    public final static int NO_ME = 0;

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public int me = 0;
    @DatabaseField
    public String type;
    @DatabaseField
    public String messageType;
    @DatabaseField
    public String content;
    @DatabaseField(dataType = DataType.DATE)
    public Date createDate;

    public MsgBody() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(messageType);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<MsgBody> CREATOR = new Parcelable.Creator<MsgBody>() {
        @Override
        public MsgBody createFromParcel(Parcel source) {
            return new MsgBody(source);
        }

        @Override
        public MsgBody[] newArray(int size) {
            return new MsgBody[size];
        }
    };

    private MsgBody(Parcel source) {
        type=source.readString();
        messageType=source.readString();
        content=source.readString();
    }

    @Override
    public String toString() {
        return "MsgBody{" +
                "id=" + id +
                ", me=" + me +
                ", type='" + type + '\'' +
                ", messageType='" + messageType + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
