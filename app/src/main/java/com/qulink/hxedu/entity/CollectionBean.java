package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CollectionBean {

    /**
     * records : [{"curriculumId":1,"collectId":3,"curriculumName":"Java生产环境下性能监控与调优详解","curriculumImage":"img1.sycdn.imooc.com/szimg/5b384772000132c405400300-500-284.jpg"},{"curriculumId":2,"collectId":4,"curriculumName":"微服务架构核心20讲","curriculumImage":"https://static001.geekbang.org/resource/image/ec/c5/ec965e5797d52a3de8ba0bdc1c38e9c5.jpg"}]
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
         * curriculumId : 1
         * collectId : 3
         * curriculumName : Java生产环境下性能监控与调优详解
         * curriculumImage : img1.sycdn.imooc.com/szimg/5b384772000132c405400300-500-284.jpg
         */

        private boolean isCheck;
        private int curriculumId;
        private int collectId;
        private String curriculumName;
        private String curriculumImage;
    }
}
