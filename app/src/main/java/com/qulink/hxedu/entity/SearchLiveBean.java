package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchLiveBean {
    /**
     * records : [{"id":3,"title":"Java基础","startTime":"2020-07-01 10:16:25","coverUrl":"xxx","participated":0,"headImage":"900a089e48566799UoYXrWsRxC3WObJcOpLhrhOun9G8OAkr","teacherName":"刘宁山","userId":12},{"id":2,"title":"C# 实战","startTime":"2020-06-04 14:24:45","coverUrl":"www.baidu.com","participated":0,"headImage":"900a089e48566799UoYXrWsRxC3WObJcOpLhrhOun9G8OAkr","teacherName":"刘宁山","userId":12},{"id":1,"title":"Java 高并发实战","startTime":"2020-06-03 12:09:14","coverUrl":"www.baidu.com","participated":0,"headImage":"900a089e48566799UoYXrWsRxC3WObJcOpLhrhOun9G8OAkr","teacherName":"刘宁山","userId":12}]
     * total : 3
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

    @NoArgsConstructor
    @Data
    public static class RecordsBean {
        /**
         * id : 3
         * title : Java基础
         * startTime : 2020-07-01 10:16:25
         * coverUrl : xxx
         * participated : 0
         * headImage : 900a089e48566799UoYXrWsRxC3WObJcOpLhrhOun9G8OAkr
         * teacherName : 刘宁山
         * userId : 12
         */

        private int status;
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
