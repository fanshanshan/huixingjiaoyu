package com.qulink.hxedu.entity;

public class TopPicBean {
    /**
     * id : 5
     * name : 热门话题3
     * disableStatus : 0
     * hotStatus : 1 热门状态
     * recStatus : 0 推荐状态
     * numbers : 0
     * createTime : 2020-05-29 09:25:07
     * updateTime : 2020-05-29 09:25:11
     */

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int id;
    private String name;
    private int disableStatus;
    private int hotStatus;
    private int recStatus;
    private int numbers;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisableStatus() {
        return disableStatus;
    }

    public void setDisableStatus(int disableStatus) {
        this.disableStatus = disableStatus;
    }

    public int getHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(int hotStatus) {
        this.hotStatus = hotStatus;
    }

    public int getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(int recStatus) {
        this.recStatus = recStatus;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
