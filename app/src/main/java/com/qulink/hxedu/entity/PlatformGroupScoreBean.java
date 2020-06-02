package com.qulink.hxedu.entity;

import java.util.List;

public class PlatformGroupScoreBean {

    /**
     * rs : [{"name":"积分项2","id":2,"credit":5,"status":0},{"name":"积分项1","id":1,"credit":10,"status":0}]
     * todayCredit : 0
     */

    private int todayCredit;
    private List<RsBean> rs;

    public int getTodayCredit() {
        return todayCredit;
    }

    public void setTodayCredit(int todayCredit) {
        this.todayCredit = todayCredit;
    }

    public List<RsBean> getRs() {
        return rs;
    }

    public void setRs(List<RsBean> rs) {
        this.rs = rs;
    }

    public static class RsBean {
        /**
         * name : 积分项2
         * id : 2
         * credit : 5
         * status : 0
         */

        private String name;
        private int id;
        private int credit;
        private int status;

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

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isComplete(){
            if(getStatus()==0)return false;else return true;
        }
    }
}
