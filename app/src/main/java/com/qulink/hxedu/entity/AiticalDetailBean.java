package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AiticalDetailBean {
    /**
     * CommentDetailsList : [{"id":19,"content":"666","userId":12,"createTime":"2020-06-09 19:33:09","thumbs":1,"nickname":"傻狗2","headImg":"900a089e485667993ALn8ha7Tt3Bk1sUQEjRKYJ0O045vNlt","status":0,"replyList":[{"id":79,"content":"777","fromUserId":12,"toUserId":12,"fromUserName":"傻狗2","toUserName":"傻狗2"}]}]
     * articelDetails : {"id":33,"topicId":null,"title":"睡觉","content":"困了","imgPath":"900a089e485667990qG6h3RJY2Ml1qJQYJDFlVAUngQs19b4,900a089e48566799kKM9EdHWwA0TCMvSUvCNUHRFXfAMX1wi,900a089e48566799N9IYjLestx8eVfPJM2W4fbl3uoHhNfQu,900a089e48566799ujovSXr7VvJcCCO9jB3sdC3XuKovte0x","userId":12,"createTime":"2020-06-09 00:24:38","thumbStatus":null,"comments":9,"thumbs":1,"topicName":"深夜栏目","numbers":1}
     */

    private ArticelDetailsBean articelDetails;
    private List<PicBean> CommentDetailsList;

    @NoArgsConstructor
    @Data
    public static class ArticelDetailsBean {
        /**
         * id : 33
         * topicId : null
         * title : 睡觉
         * content : 困了
         * imgPath : 900a089e485667990qG6h3RJY2Ml1qJQYJDFlVAUngQs19b4,900a089e48566799kKM9EdHWwA0TCMvSUvCNUHRFXfAMX1wi,900a089e48566799N9IYjLestx8eVfPJM2W4fbl3uoHhNfQu,900a089e48566799ujovSXr7VvJcCCO9jB3sdC3XuKovte0x
         * userId : 12
         * createTime : 2020-06-09 00:24:38
         * thumbStatus : null
         * comments : 9
         * thumbs : 1
         * topicName : 深夜栏目
         * numbers : 1
         */

        private int id;
        private Object topicId;
        private String title;
        private String content;
        private String imgPath;
        private int userId;
        private String createTime;
        private Object thumbStatus;
        private int comments;
        private int thumbs;
        private String topicName;
        private int numbers;
    }

    @NoArgsConstructor
    @Data
    public static class CommentDetailsListBean {
        /**
         * id : 19
         * content : 666
         * userId : 12
         * createTime : 2020-06-09 19:33:09
         * thumbs : 1
         * nickname : 傻狗2
         * headImg : 900a089e485667993ALn8ha7Tt3Bk1sUQEjRKYJ0O045vNlt
         * status : 0
         * replyList : [{"id":79,"content":"777","fromUserId":12,"toUserId":12,"fromUserName":"傻狗2","toUserName":"傻狗2"}]
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
             * id : 79
             * content : 777
             * fromUserId : 12
             * toUserId : 12
             * fromUserName : 傻狗2
             * toUserName : 傻狗2
             */

            private int id;
            private String content;
            private int fromUserId;
            private int toUserId;
            private String fromUserName;
            private String toUserName;
        }
    }
}
