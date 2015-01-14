package com.okar.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by snow on 14-7-31.
 */
public class ApplyMemberCardRecord {

    private int id;

    private String message;
    private String image;

    private int isDispose;
    private int disposeResult;

    private long createTime;
    private long insertDate;

    private Member member;
    private Account account;

    @Override
    public String toString() {
        return "ApplyMemberCardRecord{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", image='" + image + '\'' +
                ", isDispose=" + isDispose +
                ", disposeResult=" + disposeResult +
                ", createTime=" + createTime +
                ", member=" + member +
                ", account=" + account +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsDispose() {
        return isDispose;
    }

    public void setIsDispose(int isDispose) {
        this.isDispose = isDispose;
    }

    public int getDisposeResult() {
        return disposeResult;
    }

    public void setDisposeResult(int disposeResult) {
        this.disposeResult = disposeResult;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(long insertDate) {
        this.insertDate = insertDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
