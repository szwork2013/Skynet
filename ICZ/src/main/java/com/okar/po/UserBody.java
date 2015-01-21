package com.okar.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangfengchen on 15/1/20.
 */
public class UserBody implements Parcelable {

    public UserBody(String u, String p) {
        username = u;
        password = p;
    }

    public String username;

    public String password;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
    }

    public static final Parcelable.Creator<UserBody> CREATOR = new Parcelable.Creator<UserBody>() {
        @Override
        public UserBody createFromParcel(Parcel source) {
            return new UserBody(source);
        }

        @Override
        public UserBody[] newArray(int size) {
            return new UserBody[size];
        }
    };

    private UserBody(Parcel source) {
        username=source.readString();
        password=source.readString();
    }

}
