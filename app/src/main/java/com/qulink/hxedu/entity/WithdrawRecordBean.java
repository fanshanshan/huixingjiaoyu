package com.qulink.hxedu.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WithdrawRecordBean {
    /**
     * totalAmount : 0
     * page : {"records":[{"id":22,"userId":12,"amount":100,"outBizNo":"1274761939565809664","withdrawSn":null,"orderId":null,"type":1,"withdrawAccount":"18363815233","status":2,"failureReason":"收款用户不存在","remark":null,"paymentTime":null,"failureTime":"2020-06-22 01:51:47","createTime":"2020-06-22 01:51:47","updateTime":"2020-06-22 01:51:46","callbackTime":null},{"id":20,"userId":12,"amount":55,"outBizNo":"1273671152102936576","withdrawSn":null,"orderId":null,"type":1,"withdrawAccount":"18363815233","status":2,"failureReason":"收款用户不存在","remark":null,"paymentTime":null,"failureTime":"2020-06-19 01:37:22","createTime":"2020-06-19 01:37:22","updateTime":"2020-06-19 01:37:22","callbackTime":null},{"id":18,"userId":12,"amount":10,"outBizNo":"1273627339112189952","withdrawSn":null,"orderId":null,"type":1,"withdrawAccount":"18363815233","status":2,"failureReason":"收款用户不存在","remark":null,"paymentTime":null,"failureTime":"2020-06-18 22:43:16","createTime":"2020-06-18 22:43:16","updateTime":"2020-06-18 22:43:16","callbackTime":null}],"total":3,"size":8,"current":1,"orders":[],"hitCount":false,"searchCount":true,"pages":1}
     */

    private int totalAmount;
    private PageBean page;

    @NoArgsConstructor
    @Data
    public static class PageBean {
        /**
         * records : [{"id":22,"userId":12,"amount":100,"outBizNo":"1274761939565809664","withdrawSn":null,"orderId":null,"type":1,"withdrawAccount":"18363815233","status":2,"failureReason":"收款用户不存在","remark":null,"paymentTime":null,"failureTime":"2020-06-22 01:51:47","createTime":"2020-06-22 01:51:47","updateTime":"2020-06-22 01:51:46","callbackTime":null},{"id":20,"userId":12,"amount":55,"outBizNo":"1273671152102936576","withdrawSn":null,"orderId":null,"type":1,"withdrawAccount":"18363815233","status":2,"failureReason":"收款用户不存在","remark":null,"paymentTime":null,"failureTime":"2020-06-19 01:37:22","createTime":"2020-06-19 01:37:22","updateTime":"2020-06-19 01:37:22","callbackTime":null},{"id":18,"userId":12,"amount":10,"outBizNo":"1273627339112189952","withdrawSn":null,"orderId":null,"type":1,"withdrawAccount":"18363815233","status":2,"failureReason":"收款用户不存在","remark":null,"paymentTime":null,"failureTime":"2020-06-18 22:43:16","createTime":"2020-06-18 22:43:16","updateTime":"2020-06-18 22:43:16","callbackTime":null}]
         * total : 3
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
        public static class RecordsBean implements Serializable {
            /**
             * id : 22
             * userId : 12
             * amount : 100.0
             * outBizNo : 1274761939565809664
             * withdrawSn : null
             * orderId : null
             * type : 1
             * withdrawAccount : 18363815233
             * status : 2
             * failureReason : 收款用户不存在
             * remark : null
             * paymentTime : null
             * failureTime : 2020-06-22 01:51:47
             * createTime : 2020-06-22 01:51:47
             * updateTime : 2020-06-22 01:51:46
             * callbackTime : null
             */

            private int id;
            private int userId;
            private double amount;
            private String outBizNo;
            private String withdrawSn;
            private String orderId;
            private int type;
            private String withdrawAccount;
            private int status;
            private String failureReason;
            private String remark;
            private String paymentTime;
            private String failureTime;
            private String createTime;
            private String updateTime;
            private String callbackTime;
        }
    }
}
