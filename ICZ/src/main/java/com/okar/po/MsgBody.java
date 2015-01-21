package com.okar.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangfengchen on 15/1/20.
 */
public class MsgBody implements Parcelable{

    public final static String CHAT_TYPE = "chat";

    public final static String TEXT_MESSAGE_TYPE = "text";

    public final static int ME = 1;

    public final static int NO_ME = 0;

    public int me = 0;

    public String type;

    public String messageType;

    public String content;

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
}
