package com.okar.icz.entry;

import com.google.gson.annotations.Expose;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class Member {
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
}
