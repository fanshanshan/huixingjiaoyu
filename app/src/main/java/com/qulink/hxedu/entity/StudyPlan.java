package com.qulink.hxedu.entity;

import java.util.List;

public class StudyPlan {


    /**
     * total : 2
     * finishedTotal : 1
     * finished : [{"id":19,"userId":8,"content":"空军建军节","status":1,"createTime":"2020-06-01 10:35:59","updateTime":"2020-06-01 12:10:08","date":"2020-06-01"}]
     * unfinished : [{"id":20,"userId":8,"content":"第二个","status":0,"createTime":"2020-06-01 12:03:51","updateTime":"2020-06-01 12:03:51","date":"2020-06-01"}]
     */

    private int total;
    private int finishedTotal;
    private List<UnfinishedBean> finished;
    private List<UnfinishedBean> unfinished;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFinishedTotal() {
        return finishedTotal;
    }

    public void setFinishedTotal(int finishedTotal) {
        this.finishedTotal = finishedTotal;
    }

    public List<UnfinishedBean> getFinished() {
        return finished;
    }

    public void setFinished(List<UnfinishedBean> finished) {
        this.finished = finished;
    }

    public List<UnfinishedBean> getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(List<UnfinishedBean> unfinished) {
        this.unfinished = unfinished;
    }


    public static class UnfinishedBean {
        /**
         * id : 20
         * userId : 8
         * content : 第二个
         * status : 0
         * createTime : 2020-06-01 12:03:51
         * updateTime : 2020-06-01 12:03:51
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
    }
}
