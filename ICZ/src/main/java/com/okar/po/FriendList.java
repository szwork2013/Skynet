package com.okar.po;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wangfengchen on 15/1/21.
 */
public class FriendList implements Parcelable{

    public ArrayList<Friend> data;

    public FriendList() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
    }

    public static final Creator<FriendList> CREATOR = new Creator<FriendList>() {
        @Override
        public FriendList createFromParcel(Parcel source) {
            return new FriendList(source);
        }

        @Override
        public FriendList[] newArray(int size) {
            return new FriendList[size];
        }
    };

    private FriendList(Parcel source) {
        data = source.readArrayList(Friend.class.getClassLoader());
    }

}
