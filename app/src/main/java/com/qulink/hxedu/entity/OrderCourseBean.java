package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderCourseBean {

    /**
     * curriculumId : 2
     * curriculumImage : https://static001.geekbang.org/resource/image/ec/c5/ec965e5797d52a3de8ba0bdc1c38e9c5.jpg
     * curriculumName : 微服务架构核心20讲
     * expired : 0
     * orderType : 2
     * amount : 99.0
     * vipAmount : 80.0
     * integration : 30
     * createTime : 2020-06-18 21:49:17
     */

    private int curriculumId;
    private String curriculumImage;
    private String curriculumName;
    private int expired;
    private int orderType;
    private double amount;
    private double vipAmount;
    private int integration;
    private String createTime;
}
