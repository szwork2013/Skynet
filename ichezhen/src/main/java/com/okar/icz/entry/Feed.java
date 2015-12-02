package com.okar.icz.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class Feed implements Parcelable {

    public static final int FEED_TYPE_POST_TOPIC = 31;//发帖
    public static final int FEED_TYPE_WANTED2STICK = 34;//通缉贴
    public static final int FEED_TYPE_POST_QUESTION = 38;//问问贴

    @Expose
    int id;
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
    @Expose
    int praiseCount;
    @Expose
    int praiseState;
    @Expose
    int accountId;
    @Expose
    int feedForwardNum;
    @Expose
    int commentCount;
    @Expose
    int favourite;

    private List<String> coverList;

    int pos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getPraiseState() {
        return praiseState;
    }

    public void setPraiseState(int praiseState) {
        this.praiseState = praiseState;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getFeedForwardNum() {
        return feedForwardNum;
    }

    public void setFeedForwardNum(int feedForwardNum) {
        this.feedForwardNum = feedForwardNum;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public Feed(){}

    private Feed(Parcel in) {
        setId(in.readInt());
        setContent(in.readString());
        setPraiseCount(in.readInt());
        setPraiseState(in.readInt());
        setAccountId(in.readInt());
        setFeedForwardNum(in.readInt());
        setCommentCount(in.readInt());
        setFavourite(in.readInt());
        List<String> list = new ArrayList<>();
        in.readStringList(list);
        setCoverList(list);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getContent());
        parcel.writeInt(getPraiseCount());
        parcel.writeInt(getPraiseState());
        parcel.writeInt(getAccountId());
        parcel.writeInt(getFeedForwardNum());
        parcel.writeInt(getCommentCount());
        parcel.writeInt(getFavourite());
        parcel.writeStringList(getCoverList());
    }

    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
