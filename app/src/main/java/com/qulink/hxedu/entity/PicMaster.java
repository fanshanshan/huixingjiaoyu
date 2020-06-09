package com.qulink.hxedu.entity;

import java.io.Serializable;
import java.util.List;

public class PicMaster implements Serializable {
    /**
     * badge : []
     * headImg : null
     * level : 0
     * nickname : kkkppp
     * userId : 9
     * status : 0
     */

    private String headImg;
    private int level;
    private String nickname;
    private int userId;
    private int status;
    private List<?> badge;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<?> getBadge() {
        return badge;
    }

    public void setBadge(List<?> badge) {
        this.badge = badge;
    }
}
