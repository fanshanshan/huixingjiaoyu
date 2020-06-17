package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ArticalDetailBean {
    /**
     * CommentDetailsList : [{"id":40,"content":"了555","userId":12,"createTime":"2020-06-10 23:44:32","thumbs":0,"nickname":"傻狗2","headImg":"900a089e485667993ALn8ha7Tt3Bk1sUQEjRKYJ0O045vNlt","status":0,"replyList":[{"id":92,"content":"999","fromUserId":12,"toUserId":12,"fromUserName":"傻狗2","toUserName":"傻狗2"}]}]
     * articelDetails : {"id":35,"topicId":null,"title":"测试","content":"58875555587啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦考虑咯啦咯啦咯啦咯啦JJ图咯啦咯啦咯啦咯JJ突突突吐了咯啦咯啦咯路兔兔图图兔兔图图吐了咯突突突啦咯啦咯啦咯啦咯图突突突突突突啦咯啦咯摸摸摸摸摸摸哦哦哦哦哦哦哦哦哦哦在真学在真我找我哦哦哦哦哦突突突突突突突突突","imgPath":"900a089e48566799RBUYUACCfodLgAuR74QUpywIlkjS19oF,900a089e48566799RBUYUACCfodLgAuR74QUpywIlkjS19oF,900a089e48566799gUkbPf8MQqL0qa0xzhWQYKvUdAHuWMwq,900a089e48566799lyJAlJgh4Dr68viXZzSO5xS8lL9vZfdZ,900a089e48566799ocGtqvbvMc70AcwcZGPUUgrUy5M5brEW,900a089e48566799WbVZO2xpKycKfjNGMXlBunKngz5c1umZ,900a089e485667997Y4gQPTGhLet1rJRrqhcEB7GoJDN7eoU,900a089e48566799BfNIAa8q3BcVvdZ6CGiIi9RQ72MqsENQ,900a089e485667997Y4gQPTGhLet1rJRrqhcEB7GoJDN7eoU","userId":12,"createTime":"2020-06-10 17:05:34","thumbStatus":null,"comments":5,"thumbs":1,"topicName":"今晚打老虎","numbers":2}
     */

    private PicBean articelDetails;
    private List<CommentDetailsListBean> CommentDetailsList;

    @NoArgsConstructor
    @Data
    public static class ArticelDetailsBean {
        /**
         * id : 35
         * topicId : null
         * title : 测试
         * content : 58875555587啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦咯啦考虑咯啦咯啦咯啦咯啦JJ图咯啦咯啦咯啦咯JJ突突突吐了咯啦咯啦咯路兔兔图图兔兔图图吐了咯突突突啦咯啦咯啦咯啦咯图突突突突突突啦咯啦咯摸摸摸摸摸摸哦哦哦哦哦哦哦哦哦哦在真学在真我找我哦哦哦哦哦突突突突突突突突突
         * imgPath : 900a089e48566799RBUYUACCfodLgAuR74QUpywIlkjS19oF,900a089e48566799RBUYUACCfodLgAuR74QUpywIlkjS19oF,900a089e48566799gUkbPf8MQqL0qa0xzhWQYKvUdAHuWMwq,900a089e48566799lyJAlJgh4Dr68viXZzSO5xS8lL9vZfdZ,900a089e48566799ocGtqvbvMc70AcwcZGPUUgrUy5M5brEW,900a089e48566799WbVZO2xpKycKfjNGMXlBunKngz5c1umZ,900a089e485667997Y4gQPTGhLet1rJRrqhcEB7GoJDN7eoU,900a089e48566799BfNIAa8q3BcVvdZ6CGiIi9RQ72MqsENQ,900a089e485667997Y4gQPTGhLet1rJRrqhcEB7GoJDN7eoU
         * userId : 12
         * createTime : 2020-06-10 17:05:34
         * thumbStatus : null
         * comments : 5
         * thumbs : 1
         * topicName : 今晚打老虎
         * numbers : 2
         */
        private boolean isInitMaster;

        private PicMaster picMaster;
        private int id;
        private Object topicId;
        private String title;
        private String content;
        private String imgPath;
        private int userId;
        private String createTime;
        private int thumbStatus;
        private int comments;
        private int thumbs;
        private String topicName;
        private int numbers;
    }

    @NoArgsConstructor
    @Data
    public static class CommentDetailsListBean {
        /**
         * id : 40
         * content : 了555
         * userId : 12
         * createTime : 2020-06-10 23:44:32
         * thumbs : 0
         * nickname : 傻狗2
         * headImg : 900a089e485667993ALn8ha7Tt3Bk1sUQEjRKYJ0O045vNlt
         * status : 0
         * replyList : [{"id":92,"content":"999","fromUserId":12,"toUserId":12,"fromUserName":"傻狗2","toUserName":"傻狗2"}]
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
             * id : 92
             * content : 999
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
