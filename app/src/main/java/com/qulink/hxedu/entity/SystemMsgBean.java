package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SystemMsgBean {
    /**
     * id : 19
     * userId : 8
     * title : 会员消息
     * content : 恭喜您成功购买年费会员,更多权益任您享受
     * createTime : 2020-05-27 16:03:58
     * readStatus : null
     */

    private int id;
    private int userId;
    private String title;
    private String content;
    private String createTime;
    private int readStatus;
}
