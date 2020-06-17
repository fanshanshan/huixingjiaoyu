package com.qulink.hxedu.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NoticeDetail implements Serializable {
    /**
     * id : 2
     * title : 测试活动
     * detail : 我是测试详情
     * createTime : 2020-05-27 03:48:46
     */

    private int id;
    private String title;
    private String detail;
    private String createTime;
}
