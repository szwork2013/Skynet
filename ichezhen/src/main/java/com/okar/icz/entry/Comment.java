package com.okar.icz.entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wangfengchen on 15/12/2.
 */
public class Comment {
    @Expose
    int id;
    @Expose
    @SerializedName("account")
    int accountId;
    @Expose
    int uid;
    @Expose
    String comment;
    @Expose
    long createTime;
    @Expose
    int reUid;
    @Expose
    Member member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getReUid() {
        return reUid;
    }

    public void setReUid(int reUid) {
        this.reUid = reUid;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
