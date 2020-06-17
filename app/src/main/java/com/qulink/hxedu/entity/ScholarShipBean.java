package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ScholarShipBean {
    /**
     * amount : 450.0
     * withDrawAmount : 0.0
     * canWithDrawAmount : 1050.0
     * monthAmount : 150.0
     * list : [{"id":8,"userId":8,"operationAmount":150,"amount":600,"operationType":1,"date":"2020-06-03","createTime":"2020-06-03 16:05:15","updateTime":"2020-05-27 16:05:15"}]
     */

    private double amount;
    private double withDrawAmount;
    private double canWithDrawAmount;
    private double monthAmount;
    private List<ListBean> list;

    @NoArgsConstructor
    @Data
    public static class ListBean {
        /**
         * id : 8
         * userId : 8
         * operationAmount : 150.0
         * amount : 600.0
         * operationType : 1  1入学尹建江  2推广达人讲  3优秀助学金讲
         * date : 2020-06-03
         * createTime : 2020-06-03 16:05:15
         * updateTime : 2020-05-27 16:05:15
         */

        private int id;
        private int userId;
        private double operationAmount;
        private double amount;
        private int operationType;
        private String date;
        private String createTime;
        private String updateTime;
    }
}
