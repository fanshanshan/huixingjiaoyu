package com.qulink.hxedu.entity;

public class UserInfo {
    /**
     * phone : 18363815233
     * nickename :
     * headImg :
     * level : 1
     * status : 0
     * payPasswordStatus : 1
     * signStatus : 0
     * nicknameModifications : 0
     * wxBindStatus : 0
     */

    private String phone;
    private String nickname;
    private String headImg;
    private int level;
    private int status;
    private int payPasswordStatus;
    private int signStatus;
    private int nicknameModifications;
    private int wxBindStatus;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickename() {
        return nickname;
    }

    public void setNickename(String nickename) {
        this.nickname = nickename;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPayPasswordStatus() {
        return payPasswordStatus;
    }

    public void setPayPasswordStatus(int payPasswordStatus) {
        this.payPasswordStatus = payPasswordStatus;
    }

    public int getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(int signStatus) {
        this.signStatus = signStatus;
    }

    public int getNicknameModifications() {
        return nicknameModifications;
    }

    public void setNicknameModifications(int nicknameModifications) {
        this.nicknameModifications = nicknameModifications;
    }

    public int getWxBindStatus() {
        return wxBindStatus;
    }

    public void setWxBindStatus(int wxBindStatus) {
        this.wxBindStatus = wxBindStatus;
    }

    public boolean isSign(){
        if(this.getSignStatus()==0){
            return false;
        }
        return true;
    }
    public boolean isVip(){
        if(this.getStatus()==0){
            return false;
        }
        return true;
    }
}
