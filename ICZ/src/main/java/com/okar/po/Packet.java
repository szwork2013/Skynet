package com.okar.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangfengchen on 15/1/20.
 */
public class Packet implements Parcelable {

    public final static String LOGIN_TYPE = "auth";

    public final static String REGISTER_TYPE = "register";

    public String from;

    public String to;

    public Parcelable body;

    public String type;

    public Packet(String t) {
        type = t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(type);
        dest.writeParcelable(body, flags);
    }

    public static final Parcelable.Creator<Packet> CREATOR = new Parcelable.Creator<Packet>() {
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
        from=source.readString();
        to=source.readString();
        type=source.readString();
        body=source.readParcelable(Packet.class.getClassLoader());
    }


}
