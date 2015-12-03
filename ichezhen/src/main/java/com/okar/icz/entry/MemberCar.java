package com.okar.icz.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class MemberCar implements Parcelable {

    @Expose
    String pinpai;
    @Expose
    int brand;

    public String getPinpai() {
        return pinpai;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }


    public MemberCar(){}

    private MemberCar(Parcel in) {
        setPinpai(in.readString());
        setBrand(in.readInt());

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getPinpai());
        parcel.writeInt(getBrand());
    }

    public static final Parcelable.Creator<MemberCar> CREATOR = new Parcelable.Creator<MemberCar>() {
        public MemberCar createFromParcel(Parcel in) {
            return new MemberCar(in);
        }

        public MemberCar[] newArray(int size) {
            return new MemberCar[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
