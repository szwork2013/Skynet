package com.okar.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangfengchen on 15/1/21.
 */
public class Body implements Parcelable{
    public int id;
    public String type;
    public String key;
    public String message;

    public Body() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(key);
        dest.writeString(message);
    }

    public static final Parcelable.Creator<Body> CREATOR = new Parcelable.Creator<Body>() {
        @Override
        public Body createFromParcel(Parcel source) {
            return new Body(source);
        }

        @Override
        public Body[] newArray(int size) {
            return new Body[size];
        }
    };

    private Body(Parcel source) {
        id = source.readInt();
        type=source.readString();
        key=source.readString();
        message=source.readString();
    }
}
