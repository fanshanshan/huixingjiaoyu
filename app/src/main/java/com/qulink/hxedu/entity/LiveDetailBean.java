package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveDetailBean {
    /**
     * records : [{"id":1,"title":"Java 高并发实战","startTime":"2020-06-03 12:09:14","coverUrl":"www.baidu.com","participated":0,"headImage":"DA8B6D0E-655D-4920-A286-B1293DD2EB5B01592215900","teacherName":"kkkppp","userId":9}]
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
         * id : 1
         * title : Java 高并发实战
         * startTime : 2020-06-03 12:09:14
         * coverUrl : www.baidu.com
         * participated : 0
         * headImage : DA8B6D0E-655D-4920-A286-B1293DD2EB5B01592215900
         * teacherName : kkkppp
         * userId : 9
         */

        private int id;
        private String title;
        private String startTime;
        private String coverUrl;
        private int participated;
        private String headImage;
        private String teacherName;
        private int userId;
    }
}
