package com.brik.chatservice.entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wangfengchen on 15/1/20.
 */
public class Packet<T extends Parcelable> implements Parcelable {

    public final static String LOGIN_TYPE = "auth";

    public final static String REGISTER_TYPE = "register";

    public final static String MESSAGE_TYPE = "message";

    public final static String QUERY_TYPE = "query";

    public int from;

    public int to;

    public Parcelable body;

    public String type;

    public ArrayList<T> list;

    public Packet(String t) {
        type = t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(from);
        dest.writeInt(to);
        dest.writeString(type);
        dest.writeParcelable(body, flags);
        dest.writeList(list);
    }

    public static final Creator<Packet> CREATOR = new Creator<Packet>() {
        @Override
        public Packet createFromParcel(Parcel source) {
            return new Packet(source);
        }

        @Override
        public Packet[] newArray(int size) {
            return new Packet[size];
        }
    };

    private Packet(Parcel source) {
        from=source.readInt();
        to=source.readInt();
        type=source.readString();
        body=source.readParcelable(Packet.class.getClassLoader());
        list = source.readArrayList(Parcelable.class.getClassLoader());
    }


}
