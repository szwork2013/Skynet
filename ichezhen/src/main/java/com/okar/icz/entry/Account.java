package com.okar.icz.entry;

import com.google.gson.annotations.Expose;

/**
 * Created by wangfengchen on 15/11/26.
 */

public class Account {
    @Expose
    String groupName;
    @Expose
    String logo;
    @Expose
    String chengshi;
    @Expose
    String chexi;
    @Expose
    Member member;
    @Expose
    int feedCount;
    @Expose
    int memberCount;
    @Expose
    int score;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getChengshi() {
        return chengshi;
    }

    public void setChengshi(String chengshi) {
        this.chengshi = chengshi;
    }

    public String getChexi() {
        return chexi;
    }

    public void setChexi(String chexi) {
        this.chexi = chexi;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(int feedCount) {
        this.feedCount = feedCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
