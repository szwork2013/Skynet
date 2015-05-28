package com.okar.icz.po;

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
public class Merchant implements Parcelable{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name,cover;
    @DatabaseField(dataType = DataType.DATE)
    public Date createDate;

    public Merchant() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(cover);
    }

    public static final Creator<Merchant> CREATOR = new Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel source) {
            return new Merchant(source);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };

    private Merchant(Parcel source) {
        name=source.readString();
        cover=source.readString();
    }


}
