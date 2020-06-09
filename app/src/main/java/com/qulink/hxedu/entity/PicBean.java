package com.qulink.hxedu.entity;

import java.io.Serializable;

public class PicBean implements Serializable {

    /**
     * id : 14
     * topicId : null
     * title : 333333
     * content : [textField resignFirstResponder]; [textField resignFirstResponder];
     * imgPath :
     * userId : 9
     * createTime : 2020-06-01 15:31:25
     * thumbStatus : 0
     * comments : 0
     * thumbs : 1
     * topicName : null
     * numbers : null
     */

    private int id;

    private boolean isInitMaster;

    public boolean isInitMaster() {
        return isInitMaster;
    }

    public void setInitMaster(boolean initMaster) {
        isInitMaster = initMaster;
    }

    public PicMaster getPicMaster() {
        return picMaster;
    }

    public void setPicMaster(PicMaster picMaster) {
        this.picMaster = picMaster;
    }

    private PicMaster picMaster;
    private Object topicId;
    private String title;
    private String content;
    private String imgPath;
    private int userId;
    private String createTime;
    private int thumbStatus;
    private int comments;
    private int thumbs;
    private String topicName;
    private Object numbers;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getTopicId() {
        return topicId;
    }

    public void setTopicId(Object topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getThumbStatus() {
        return thumbStatus;
    }

    public void setThumbStatus(int thumbStatus) {
        this.thumbStatus = thumbStatus;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getThumbs() {
        return thumbs;
    }

    public void setThumbs(int thumbs) {
        this.thumbs = thumbs;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Object getNumbers() {
        return numbers;
    }

    public void setNumbers(Object numbers) {
        this.numbers = numbers;
    }
}
