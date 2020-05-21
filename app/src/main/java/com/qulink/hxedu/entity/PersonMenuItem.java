package com.qulink.hxedu.entity;

public class PersonMenuItem {

   private int img;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PersonMenuItem(int img, String title) {
        this.img = img;
        this.title = title;
    }

    private String title;
}
