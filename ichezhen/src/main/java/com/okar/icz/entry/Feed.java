package com.okar.icz.entry;

import com.google.gson.annotations.Expose;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class Feed {
    @Expose
    String content;
    @Expose
    Account account;
    @Expose
    Member member;
    @Expose
    int type;
    @Expose
    long createTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
