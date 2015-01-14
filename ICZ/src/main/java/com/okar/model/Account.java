package com.okar.model;

import com.works.skynet.common.utils.EncryptUtils;
import com.works.skynet.common.utils.Utils;

import java.io.Serializable;

/**
 * Created by snow on 14-7-30.
 */
public class Account implements Serializable {

    public Account() {

    }

    public Account(int id) {
        this.id = id;
    }

    protected String verifyCode;

    protected int id;
    protected String username;
    protected String password;

    protected String wxUsername;
    protected String wxPassword;

    protected String email;
    protected String mobile;

    protected String mpid;

    protected int score;
    protected int gold;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        if (Utils.isBlank(this.password)) {
            return this.password;
        }
        return EncryptUtils.MD5(password);
    }

    public String getOriginalPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWxUsername() {
        return wxUsername;
    }

    public void setWxUsername(String wxUsername) {
        this.wxUsername = wxUsername;
    }

    public String getWxPassword() {
        return wxPassword;
    }

    public void setWxPassword(String wxPassword) {
        this.wxPassword = wxPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMpid() {
        return mpid;
    }

    public void setMpid(String mpid) {
        this.mpid = mpid;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "Account{" +
                "verifyCode='" + verifyCode + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", wxUsername='" + wxUsername + '\'' +
                ", wxPassword='" + wxPassword + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mpid='" + mpid + '\'' +
                ", score=" + score +
                '}';
    }
}

