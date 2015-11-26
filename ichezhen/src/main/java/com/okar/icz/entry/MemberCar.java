package com.okar.icz.entry;

import com.google.gson.annotations.Expose;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class MemberCar {

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
}
