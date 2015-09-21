package com.okar.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangfengchen on 15/1/21.
 */
public class Friend implements Parcelable{

    public int id;
    public String name;

    public Friend() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    private Friend(Parcel source) {
        id = source.readInt();
        name=source.readString();
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
