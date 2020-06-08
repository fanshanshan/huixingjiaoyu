package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SubjectBean {
    /**
     * id : 11
     * userId : 0
     * name : eeeeeeeeeeeeeeeeee
     * disableStatus : 0
     * hotStatus : 0
     * recStatus : 0
     * numbers : 0
     * createTime : 2020-06-01 15:20:20
     * updateTime : 2020-06-01 15:20:20
     */

    private int id;
    private int userId;
    private String name;
    private int disableStatus;
    private int hotStatus;
    private int recStatus;
    private int numbers;
    private String createTime;
    private String updateTime;
}
