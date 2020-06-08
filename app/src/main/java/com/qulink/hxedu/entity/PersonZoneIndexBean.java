package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PersonZoneIndexBean {
    /**
     * badge : [{"id":10,"userId":8,"type":1,"name":"我爱学习","createTime":"2020-06-01 11:53:21"},{"id":11,"userId":8,"type":2,"name":"学习如风,常随我动","createTime":"2020-06-01 11:53:38"},{"id":12,"userId":8,"type":3,"name":"就是学习本人","createTime":"2020-06-01 11:53:49"},{"id":13,"userId":8,"type":4,"name":"评论员","createTime":"2020-06-01 11:54:03"},{"id":14,"userId":8,"type":5,"name":"特邀评论员","createTime":"2020-06-01 11:54:15"},{"id":15,"userId":8,"type":6,"name":"特级特邀评论员","createTime":"2020-06-03 14:38:49"},{"id":16,"userId":8,"type":7,"name":"冉冉升起的分享之星","createTime":"2020-06-03 14:38:59"},{"id":17,"userId":8,"type":8,"name":"分享界的扛把子","createTime":"2020-06-03 14:39:11"},{"id":18,"userId":8,"type":9,"name":"分享界的创造之神","createTime":"2020-06-03 14:39:25"}]
     * comments : 0 评论数量
     * headImg : FgtbwgrQs_8JwGSPRONIsKA_1uhz
     * level : 1
     * nickname : 傻狗
     * userId : 8
     * articles : 0 我的贴子总数
     * status : 1 会员状态
     * thumbs : 0 点赞数量
     */

    private int comments;
    private String headImg;
    private int level;
    private String nickname;
    private int userId;
    private int articles;
    private int status;
    private int thumbs;
    private List<BadgeBean> badge;

    @NoArgsConstructor
    @Data
    public static class BadgeBean {
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
