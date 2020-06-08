package com.qulink.hxedu.entity;

import java.util.List;

public class HotCourseBean {
    /**
     * current : 1
     * pages : 1
     * records : [{"curriculumImage":"https://huixing-test-assets.kuaiyunma.com/RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg","curriculumIntro":"短视频鉴赏","curriculumName":"短视频鉴赏","curriculumPrice":100,"curriculumStatus":1,"freeStatus":1,"id":1,"participantNum":123,"payStatus":1,"popularStatus":1,"priceStatus":1,"specialStatus":1,"vipPrice":50},{"curriculumImage":"https://huixing-test-assets.kuaiyunma.com/RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg","curriculumIntro":"美女视频鉴赏","curriculumName":"美女视频鉴赏","curriculumPrice":200,"curriculumStatus":1,"freeStatus":0,"id":2,"participantNum":null,"payStatus":1,"popularStatus":0,"priceStatus":1,"specialStatus":1,"vipPrice":150}]
     * searchCount : true
     * size : 10
     * total : 2
     */

    private int current;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private List<RecordsBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * curriculumImage : https://huixing-test-assets.kuaiyunma.com/RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg
         * curriculumIntro : 短视频鉴赏
         * curriculumName : 短视频鉴赏
         * curriculumPrice : 100.0
         * curriculumStatus : 1
         * freeStatus : 1
         * id : 1
         * participantNum : 123
         * payStatus : 1
         * popularStatus : 1
         * priceStatus : 1
         * specialStatus : 1
         * vipPrice : 50.0
         */

        private String curriculumImage;
        private String curriculumIntro;
        private String curriculumName;
        private double curriculumPrice;
        private int curriculumStatus;
        private int freeStatus;
        private int id;
        private int participantNum;
        private int payStatus;
        private int popularStatus;
        private int priceStatus;
        private int specialStatus;
        private double vipPrice;


        public boolean isVipFree(){
            if(getPriceStatus()==0){
                return  false;
            }
            return  true;
        }public boolean isVipSpecial(){
            if(getSpecialStatus()==0){
                return  false;
            }
            return  true;
        }

        public boolean isFree(){
            if(getPayStatus()==0){
                return  true;
            }
            return  false;
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

        public String getCurriculumName() {
            return curriculumName;
        }

        public void setCurriculumName(String curriculumName) {
            this.curriculumName = curriculumName;
        }

        public double getCurriculumPrice() {
            return curriculumPrice;
        }

        public void setCurriculumPrice(double curriculumPrice) {
            this.curriculumPrice = curriculumPrice;
        }

        public int getCurriculumStatus() {
            return curriculumStatus;
        }

        public void setCurriculumStatus(int curriculumStatus) {
            this.curriculumStatus = curriculumStatus;
        }

        public int getFreeStatus() {
            return freeStatus;
        }

        public void setFreeStatus(int freeStatus) {
            this.freeStatus = freeStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParticipantNum() {
            return participantNum;
        }

        public void setParticipantNum(int participantNum) {
            this.participantNum = participantNum;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public int getPopularStatus() {
            return popularStatus;
        }

        public void setPopularStatus(int popularStatus) {
            this.popularStatus = popularStatus;
        }

        public int getPriceStatus() {
            return priceStatus;
        }

        public void setPriceStatus(int priceStatus) {
            this.priceStatus = priceStatus;
        }

        public int getSpecialStatus() {
            return specialStatus;
        }

        public void setSpecialStatus(int specialStatus) {
            this.specialStatus = specialStatus;
        }

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }
    }
}
