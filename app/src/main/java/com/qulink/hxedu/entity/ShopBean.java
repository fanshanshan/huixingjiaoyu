package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ShopBean {

    /**
     * records : [{"id":1,"curriculumId":1,"curriculumName":"短视频鉴赏","curriculumImage":"2ebf3a0320e98a2eiWWX310MKVwaYnM7WD8NPCrvobwMStvS","integration":300,"payAmount":200,"vipAmount":100,"exchangeType":2},{"id":2,"curriculumId":2,"curriculumName":"Java","curriculumImage":"2ebf3a0320e98a2eiWWX310MKVwaYnM7WD8NPCrvobwMStvS","integration":300,"payAmount":0,"vipAmount":0,"exchangeType":1}]
     * total : 2
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

    @NoArgsConstructor
    @Data
    public static class RecordsBean {
        /**
         * id : 1
         * curriculumId : 1
         * curriculumName : 短视频鉴赏
         * curriculumImage : 2ebf3a0320e98a2eiWWX310MKVwaYnM7WD8NPCrvobwMStvS
         * integration : 300
         * payAmount : 200.0
         * vipAmount : 100.0
         * exchangeType : 2
         */

        private int id;
        private int curriculumId;
        private String curriculumName;
        private String curriculumImage;
        private int integration;
        private double payAmount;
        private double vipAmount;
        private int exchangeType;
    }
}
