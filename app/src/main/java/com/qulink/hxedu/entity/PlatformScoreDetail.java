package com.qulink.hxedu.entity;

import java.util.List;

public class PlatformScoreDetail {

    /**
     * pscList : [{"name":"总经理积分","id":1},{"name":"区域经理积分","id":2},{"name":"临时奖励积分","id":3}]
     * credit : 539
     * commitStatus : 1
     * pccList : [{"id":1,"name":"小组活动","createTime":"2020-05-28 17:00:53"},{"id":2,"name":"每日工作","createTime":"2020-05-28 17:01:11"},{"id":3,"name":"课程会议","createTime":"2020-05-28 17:01:35"}]
     */

    private int credit;
    private int commitStatus;
    private List<PscListBean> pscList;
    private List<PccListBean> pccList;

    public boolean isCommit(){
        if(getCommitStatus()==1)return false;else return true;
    }
    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCommitStatus() {
        return commitStatus;
    }

    public void setCommitStatus(int commitStatus) {
        this.commitStatus = commitStatus;
    }

    public List<PscListBean> getPscList() {
        return pscList;
    }

    public void setPscList(List<PscListBean> pscList) {
        this.pscList = pscList;
    }

    public List<PccListBean> getPccList() {
        return pccList;
    }

    public void setPccList(List<PccListBean> pccList) {
        this.pccList = pccList;
    }

    public static class PscListBean {
        /**
         * name : 总经理积分
         * id : 1
         */

        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class PccListBean {
        /**
         * id : 1
         * name : 小组活动
         * createTime : 2020-05-28 17:00:53
         */

        private int id;
        private String name;
        private String createTime;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
