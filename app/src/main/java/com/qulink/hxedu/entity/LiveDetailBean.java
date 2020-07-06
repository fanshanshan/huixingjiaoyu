package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveDetailBean {


    /**
     * records : [{"id":2,"title":"C# 实战","startTime":"2020-06-04 14:24:45","coverUrl":"www.baidu.com","participated":0,"headImage":"900a089e48566799UoYXrWsRxC3WObJcOpLhrhOun9G8OAkr","teacherName":"刘宁山","userId":12,"status":1,"type":1,"payAmount":0,"vipAmount":0}]
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
         * id : 2
         * title : C# 实战
         * startTime : 2020-06-04 14:24:45
         * coverUrl : www.baidu.com
         * participated : 0
         * headImage : 900a089e48566799UoYXrWsRxC3WObJcOpLhrhOun9G8OAkr
         * teacherName : 刘宁山
         * userId : 12
         * status : 1
         * type : 1
         * payAmount : 0.0
         * vipAmount : 0.0
         */

        private int id;
        private String title;
        private String startTime;
        private String coverUrl;
        private int participated;
        private String headImage;
        private String teacherName;
        private int userId;
        private int status;
        private int type;
        private double payAmount;
        private double vipAmount;
    }
}
