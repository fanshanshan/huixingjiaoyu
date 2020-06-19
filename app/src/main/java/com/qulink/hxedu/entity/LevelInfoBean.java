package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LevelInfoBean {
    /**
     * id : null
     * phone : 18363815233
     * nickname : 傻狗2
     * headImg : 900a089e485667993ALn8ha7Tt3Bk1sUQEjRKYJ0O045vNlt
     * level : 0
     * credit : null
     * status : null
     * payPasswordStatus : null
     * signStatus : null
     * nicknameModifications : null
     * wxBindStatus : null
     * realAuthStatus : null
     * platformStatus : null
     * arriveDate : null
     * boughtStatus : null
     * registerDays : 16
     * percentage : 0
     * needExperience : 68
     * viewedVideoExperience : 15
     * topicsExperience : 0
     */

    private int id;
    private String phone;
    private String nickname;
    private String headImg;
    private int level;
    private String credit;
    private int status;
    private Object payPasswordStatus;
    private Object signStatus;
    private Object nicknameModifications;
    private Object wxBindStatus;
    private Object realAuthStatus;
    private Object platformStatus;
    private Object arriveDate;
    private Object boughtStatus;
    private int registerDays;
    private String percentage;
    private int needExperience;
    private int viewedVideoExperience;
    private int topicsExperience;
}
