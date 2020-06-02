package com.qulink.hxedu.entity;

import java.util.List;

public class SignDetailEntity {

    /**
     * signDays : 1
     * viewedStatus : 0
     * learningPlan : [{"id":19,"userId":8,"content":"空军建军节","status":1,"createTime":"2020-06-01 10:35:59","updateTime":"2020-06-01 12:10:08","date":"2020-06-01"},{"id":20,"userId":8,"content":"第二个","status":1,"createTime":"2020-06-01 12:03:51","updateTime":"2020-06-01 12:15:36","date":"2020-06-01"},{"id":21,"userId":8,"content":"第三个","status":1,"createTime":"2020-06-01 12:23:05","updateTime":"2020-06-01 12:23:08","date":"2020-06-01"},{"id":22,"userId":8,"content":"第四个","status":1,"createTime":"2020-06-01 12:29:07","updateTime":"2020-06-01 12:29:32","date":"2020-06-01"},{"id":23,"userId":8,"content":"二五个","status":1,"createTime":"2020-06-01 12:29:58","updateTime":"2020-06-01 12:30:01","date":"2020-06-01"},{"id":24,"userId":8,"content":"666666666","status":1,"createTime":"2020-06-01 14:15:19","updateTime":"2020-06-01 14:15:22","date":"2020-06-01"},{"id":25,"userId":8,"content":"777","status":1,"createTime":"2020-06-01 14:15:52","updateTime":"2020-06-01 14:15:56","date":"2020-06-01"},{"id":26,"userId":8,"content":"8888888","status":1,"createTime":"2020-06-01 14:18:08","updateTime":"2020-06-01 14:18:10","date":"2020-06-01"}]
     * todayCredit : 8
     * thumbStatus : 0
     * credit : 538
     * commentStatus : 0
     * shareStatus : 0
     * signRecord : ["2020-06-01"]
     */

    private int signDays;
    private int viewedStatus;
    private int todayCredit;
    private int thumbStatus;
    private int credit;
    private int commentStatus;
    private int shareStatus;
    private List<LearningPlanBean> learningPlan;
    private List<String> signRecord;

    /**
     * viewedStatus : 0  观看状态
     * learningPlan : [] 学习计划
     * todayCredit : 0  今日积分
     * thumbStatus : 0  点赞状态
     * credit : 5  积分
     * commentStatus : 0 评论状态
     * shareStatus : 0  分享状态
     * signRecord : ["2020-05-27"]  签到记录
     */



    public boolean watchVideoComplete(){
        if(viewedStatus==1){
            return true;
        }
        return  false;
    }

    public boolean commonComplete(){
        if(commentStatus==1){
            return true;
        }
        return  false;
    }

    public boolean greatComplete(){
        if(thumbStatus==1){
            return true;
        }
        return  false;
    }
    public boolean shareComplete(){
        if(shareStatus==1){
            return true;
        }
        return  false;
    }

    public int getSignDays() {
        return signDays;
    }

    public void setSignDays(int signDays) {
        this.signDays = signDays;
    }

    public int getViewedStatus() {
        return viewedStatus;
    }

    public void setViewedStatus(int viewedStatus) {
        this.viewedStatus = viewedStatus;
    }

    public int getTodayCredit() {
        return todayCredit;
    }

    public void setTodayCredit(int todayCredit) {
        this.todayCredit = todayCredit;
    }

    public int getThumbStatus() {
        return thumbStatus;
    }

    public void setThumbStatus(int thumbStatus) {
        this.thumbStatus = thumbStatus;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(int shareStatus) {
        this.shareStatus = shareStatus;
    }

    public List<LearningPlanBean> getLearningPlan() {
        return learningPlan;
    }

    public void setLearningPlan(List<LearningPlanBean> learningPlan) {
        this.learningPlan = learningPlan;
    }

    public List<String> getSignRecord() {
        return signRecord;
    }

    public void setSignRecord(List<String> signRecord) {
        this.signRecord = signRecord;
    }

    public static class LearningPlanBean {
        /**
         * id : 19
         * userId : 8
         * content : 空军建军节
         * status : 1
         * createTime : 2020-06-01 10:35:59
         * updateTime : 2020-06-01 12:10:08
         * date : 2020-06-01
         */

        private int id;
        private int userId;
        private String content;
        private int status;
        private String createTime;
        private String updateTime;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isComplete(){
            if(status==0){
                return false;
            }
            return  true;
        }
    }
}
