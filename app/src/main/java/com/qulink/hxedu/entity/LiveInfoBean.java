package com.qulink.hxedu.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveInfoBean implements Serializable {
    /**
     * title : Java 高并发实战
     * intro : 我是测试内容
     * coverUrl : www.baidu.com
     * type : 3
     * payAmount : 100.0
     * vipAmount : 90.0
     * participantTotal : 0
     * participated : 0
     * status : 1
     * startTime : 2020-06-03 12:09:14
     * headImage : DA8B6D0E-655D-4920-A286-B1293DD2EB5B01592215900
     * teacherIntro : null
     * teacherName : kkkppp
     * userId : 9
     */

    private String title;
    private String intro;
    private String coverUrl;
    private int type;
    private double payAmount;
    private double vipAmount;
    private int participantTotal;
    private int id;
    private int participated;
    private int status;
    private String startTime;
    private String headImage;
    private String teacherIntro;
    private String teacherName;
    private int userId;
}
