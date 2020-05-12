package com.qulink.hxedu.entity;

public class CustomData {

    private String img;
    private double price;
    private int joinNum;
    private String desc;
    private String title;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CustomData(String img, double price, int joinNum, String desc, String title) {
        this.img = img;
        this.price = price;
        this.joinNum = joinNum;
        this.desc = desc;
        this.title = title;
    }
}
