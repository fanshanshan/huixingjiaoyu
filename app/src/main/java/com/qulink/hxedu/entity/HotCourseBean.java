package com.qulink.hxedu.entity;

import java.util.List;

public class HotCourseBean {

    /**
     * records : [{"id":1,"curriculumName":"短视频鉴赏","curriculumImage":"RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg","curriculumIntro":"短视频鉴赏","curriculumStatus":1,"priceStatus":1,"curriculumPrice":100,"vipPrice":50,"participantNum":123,"popularStatus":1,"freeStatus":0,"payStatus":1,"specialStatus":1}]
     * total : 1
     * size : 4
     * current : 1
     * orders : []
     * hitCount : false
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean hitCount;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;
    private List<?> orders;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isHitCount() {
        return hitCount;
    }

    public void setHitCount(boolean hitCount) {
        this.hitCount = hitCount;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public List<?> getOrders() {
        return orders;
    }

    public void setOrders(List<?> orders) {
        this.orders = orders;
    }

    public static class RecordsBean {
        /**
         * id : 1
         * curriculumName : 短视频鉴赏
         * curriculumImage : RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg
         * curriculumIntro : 短视频鉴赏
         * curriculumStatus : 1
         * priceStatus : 1
         * curriculumPrice : 100.0
         * vipPrice : 50.0
         * participantNum : 123
         * popularStatus : 1
         * freeStatus : 0
         * payStatus : 1
         * specialStatus : 1
         */

        private int id;
        private String curriculumName;
        private String curriculumImage;
        private String curriculumIntro;
        private int curriculumStatus;
        private int priceStatus;
        private double curriculumPrice;
        private double vipPrice;
        private int participantNum;
        private int popularStatus;
        private int freeStatus;
        private int payStatus;
        private int specialStatus;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurriculumName() {
            return curriculumName;
        }

        public void setCurriculumName(String curriculumName) {
            this.curriculumName = curriculumName;
        }

        public String getCurriculumImage() {
            return curriculumImage;
        }

        public void setCurriculumImage(String curriculumImage) {
            this.curriculumImage = curriculumImage;
        }

        public String getCurriculumIntro() {
            return curriculumIntro;
        }

        public void setCurriculumIntro(String curriculumIntro) {
            this.curriculumIntro = curriculumIntro;
        }

        public int getCurriculumStatus() {
            return curriculumStatus;
        }

        public void setCurriculumStatus(int curriculumStatus) {
            this.curriculumStatus = curriculumStatus;
        }

        public int getPriceStatus() {
            return priceStatus;
        }

        public void setPriceStatus(int priceStatus) {
            this.priceStatus = priceStatus;
        }

        public double getCurriculumPrice() {
            return curriculumPrice;
        }

        public void setCurriculumPrice(double curriculumPrice) {
            this.curriculumPrice = curriculumPrice;
        }

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public int getParticipantNum() {
            return participantNum;
        }

        public void setParticipantNum(int participantNum) {
            this.participantNum = participantNum;
        }

        public int getPopularStatus() {
            return popularStatus;
        }

        public void setPopularStatus(int popularStatus) {
            this.popularStatus = popularStatus;
        }

        public int getFreeStatus() {
            return freeStatus;
        }

        public void setFreeStatus(int freeStatus) {
            this.freeStatus = freeStatus;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public int getSpecialStatus() {
            return specialStatus;
        }

        public void setSpecialStatus(int specialStatus) {
            this.specialStatus = specialStatus;
        }
    }
}
