package com.qulink.hxedu.entity;

public class HotArtical {
    /**
     * id : 1
     * topicId : 7
     * title : 这里是平台置顶话题
     * content : 这里是平台置顶话题
     * imgPath : null
     * userId : null
     * type : 0
     * sort : 1
     * createTime : 2020-05-29 09:27:30
     * disableStatus : 0
     * comments : 10
     * thumbs : 10
     */

    private int id;
    private int topicId;
    private String title;
    private String content;
    private Object imgPath;
    private Object userId;
    private int type;
    private int sort;
    private String createTime;
    private int disableStatus;
    private int comments;
    private int thumbs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
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

    public Object getImgPath() {
        return imgPath;
    }

    public void setImgPath(Object imgPath) {
        this.imgPath = imgPath;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDisableStatus() {
        return disableStatus;
    }

    public void setDisableStatus(int disableStatus) {
        this.disableStatus = disableStatus;
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
}
