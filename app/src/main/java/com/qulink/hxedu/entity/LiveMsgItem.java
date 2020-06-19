package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveMsgItem {
    /**
     * records : [{"id":3,"sourceId":null,"coverUrl":"休闲鞋","title":"休闲鞋","startTime":"2020-06-18 13:35:45","endTime":"2020-06-20 13:35:47","noticeTime":null,"createTime":"2020-06-18 13:35:54"}]
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

    @NoArgsConstructor
    @Data
    public static class RecordsBean {
        /**
         * id : 3
         * sourceId : null
         * coverUrl : 休闲鞋
         * title : 休闲鞋
         * startTime : 2020-06-18 13:35:45
         * endTime : 2020-06-20 13:35:47
         * noticeTime : null
         * createTime : 2020-06-18 13:35:54
         */

        private int id;
        private Object sourceId;
        private String coverUrl;
        private String title;
        private String startTime;
        private String endTime;
        private Object noticeTime;
        private String createTime;
    }
}
