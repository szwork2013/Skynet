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


}
