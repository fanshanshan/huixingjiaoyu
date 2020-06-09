package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentsBean {

    /**
     * id : 13
     * content : 吞吞吐吐吞吞吐吐
     * userId : 9
     * createTime : 2020-06-03 17:50:10
     * thumbs : 0
     * nickname : kkkppp
     * headImg : image/ios/0/1591167641
     * status : 0
     * replyList : [{"id":15,"content":"你在吞吐什么","fromUserId":1,"toUserId":9,"fromUserName":"456","toUserName":"kkkppp"},{"id":43,"content":"摸摸","fromUserId":null,"toUserId":9,"fromUserName":null,"toUserName":"kkkppp"},{"id":51,"content":"德德","fromUserId":9,"toUserId":9,"fromUserName":"kkkppp","toUserName":"kkkppp"},{"id":52,"content":"不会在任何时候","fromUserId":9,"toUserId":1,"fromUserName":"kkkppp","toUserName":"456"}]
     */

    private int id;
    private String content;
    private int userId;
    private String createTime;
    private int thumbs;
    private String nickname;
    private String headImg;
    private int status;
    private List<ReplyListBean> replyList;

    @NoArgsConstructor
    @Data
    public static class ReplyListBean {
        /**
         * id : 15
         * content : 你在吞吐什么
         * fromUserId : 1
         * toUserId : 9
         * fromUserName : 456
         * toUserName : kkkppp
         */

        private int id;
        private String content;
        private int fromUserId;
        private int toUserId;
        private String fromUserName;
        private String toUserName;
    }
}
