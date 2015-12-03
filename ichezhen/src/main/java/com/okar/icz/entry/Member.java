package com.okar.icz.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class Member implements Parcelable {
    @Expose
    int id;
    @Expose
    String nickname;
    @Expose
    String head;
    @Expose
    MemberCar car;
    @Expose
    int level;
    @Expose
    int admin;
    @Expose
    String chengshi;
    @Expose
    int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public MemberCar getCar() {
        return car;
    }

    public void setCar(MemberCar car) {
        this.car = car;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getChengshi() {
        return chengshi;
    }

    public void setChengshi(String chengshi) {
        this.chengshi = chengshi;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Member(){}

    private Member(Parcel in) {
        setId(in.readInt());
        setNickname(in.readString());
        setHead(in.readString());
        setCar(in.<MemberCar>readParcelable(getClass().getClassLoader()));
        setLevel(in.readInt());
        setAdmin(in.readInt());
        setChengshi(in.readString());
        setGender(in.readInt());

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getNickname());
        parcel.writeString(getHead());
        parcel.writeParcelable(getCar(), i);
        parcel.writeInt(getLevel());
        parcel.writeInt(getAdmin());
        parcel.writeString(getChengshi());
        parcel.writeInt(getGender());

    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
