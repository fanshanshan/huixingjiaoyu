package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BadgeBean {


    /**
     * id : null
     * phone : 18363815235
     * nickname : 傻狗
     * headImg : FgtbwgrQs_8JwGSPRONIsKA_1uhz
     * level : null
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
     * registerDays : 24
     * percentage : 0
     * total : 9
     * badges : [{"id":10,"userId":8,"type":1,"name":"我爱学习","createTime":"2020-06-01 11:53:21"},{"id":11,"userId":8,"type":2,"name":"学习如风,常随我动","createTime":"2020-06-01 11:53:38"},{"id":12,"userId":8,"type":3,"name":"就是学习本人","createTime":"2020-06-01 11:53:49"},{"id":13,"userId":8,"type":4,"name":"评论员","createTime":"2020-06-01 11:54:03"},{"id":14,"userId":8,"type":5,"name":"特邀评论员","createTime":"2020-06-01 11:54:15"},{"id":15,"userId":8,"type":6,"name":"特级特邀评论员","createTime":"2020-06-03 14:38:49"},{"id":16,"userId":8,"type":7,"name":"冉冉升起的分享之星","createTime":"2020-06-03 14:38:59"},{"id":17,"userId":8,"type":8,"name":"分享界的扛把子","createTime":"2020-06-03 14:39:11"},{"id":18,"userId":8,"type":9,"name":"分享界的创造之神","createTime":"2020-06-03 14:39:25"}]
     */

    private Object id;
    private String phone;
    private String nickname;
    private String headImg;
    private Object level;
    private Object credit;
    private Object status;
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
    private int total;
    private List<BadgesBean> badges;

    @NoArgsConstructor
    @Data
    public static class BadgesBean {
        /**
         * id : 10
         * userId : 8
         * type : 1
         * name : 我爱学习
         * createTime : 2020-06-01 11:53:21
         */

        private int id;
        private int userId;
        private int type;
        private String name;
        private String createTime;
    }
}
