package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ActivityDetailBean {
    /**
     * id : 1
     * title : 更新测试111
     * intro : 我是介绍
     * detail : 我是测试详情
     * createTime : 2020-05-25 01:01:36
     * payRequired : 0
     * payAmount : 10.0
     * startDate : 2020-05-26
     * endDate : 2020-06-30
     * participateTotal : 3
     * type : 0
     * participated : 0
     * lastRecord : {"wxNumber":"123123","mobileNumber":"15592294006"}
     */

    private int id;
    private String title;
    private String intro;
    private String detail;
    private String createTime;
    private int payRequired;
    private double payAmount;
    private String startDate;
    private String endDate;
    private int participateTotal;
    private int type;
    private int participated;
    private LastRecordBean lastRecord;

    @NoArgsConstructor
    @Data
    public static class LastRecordBean {
        /**
         * wxNumber : 123123
         * mobileNumber : 15592294006
         */

        private String wxNumber;
        private String mobileNumber;
    }
}
