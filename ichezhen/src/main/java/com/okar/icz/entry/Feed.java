package com.okar.icz.entry;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class Feed {

    public static final int FEED_TYPE_POST_TOPIC = 31;//发帖
    public static final int FEED_TYPE_WANTED2STICK = 34;//通缉贴
    public static final int FEED_TYPE_POST_QUESTION = 38;//问问贴

    @Expose
    String content;
    @Expose
    String cover;
    @Expose
    Account account;
    @Expose
    Member member;
    @Expose
    int type;
    @Expose
    long createTime;

    private List<String> coverList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public List<String> getCoverList() {
        return coverList;
    }

    public void setCoverList(List<String> coverList) {
        this.coverList = coverList;
    }
}
