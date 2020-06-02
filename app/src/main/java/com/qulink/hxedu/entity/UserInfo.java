package com.qulink.hxedu.entity;

public class UserInfo {

    /**
     * phone : 18363815233
     * nickname : 傻狗
     * headImg : null
     * level : 1
     * status : 1
     * payPasswordStatus : 1
     * signStatus : 1
     * nicknameModifications : 修改昵称次数
     * wxBindStatus : 微信绑定状态
     * platformStatus : 是否是平台账号
     * arriveDate : 2021-05-27
     * boughtStatus : 是否购买过会员
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
    private int platformStatus;
    private String arriveDate;
    private int boughtStatus;

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
    public boolean isBoughtVip(){
        if(this.getBoughtStatus()==0){
            return false;
        }
        return true;
    }

    public boolean isPlatformAccount(){
        if(this.getPlatformStatus()==0){
            return false;
        }
        return true;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(int platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public int getBoughtStatus() {
        return boughtStatus;
    }

    public void setBoughtStatus(int boughtStatus) {
        this.boughtStatus = boughtStatus;
    }
}
