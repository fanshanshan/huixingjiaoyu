package com.qulink.hxedu.entity;

import java.util.List;

@lombok.NoArgsConstructor
@lombok.Data
public class CourseBean {

    /**
     * records : [{"id":1,"curriculumName":"短视频鉴赏","curriculumImage":"RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg","curriculumIntro":"短视频鉴赏","curriculumStatus":1,"priceStatus":1,"curriculumPrice":100,"vipPrice":50,"participantNum":3,"popularStatus":1,"freeStatus":0,"payStatus":1,"specialStatus":1}]
     * total : 1
     * size : 8
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

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class RecordsBean {
        /**
         * id : 1
         * curriculumName : 短视频鉴赏
         * curriculumImage : RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg
         * curriculumIntro : 短视频鉴赏
         * curriculumStatus : 1
         * priceStatus : 1
         * curriculumPrice : 100
         * vipPrice : 50
         * participantNum : 3
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
        private int curriculumPrice;
        private int vipPrice;
        private int participantNum;
        private int popularStatus;
        private int freeStatus;
        private int payStatus;
        private int specialStatus;

        public boolean isVipFree() {
            if (getFreeStatus() == 0) {
                return false;
            }
            return true;
        }

        public boolean isVipSpecial() {
            if (getSpecialStatus() == 0) {
                return false;
            }
            return true;
        }
    }
}
