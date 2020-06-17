package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ZongMsgSubBean {
    /**
     * articleId : 20
     * nickname : kkkppp
     * headImg : DA8B6D0E-655D-4920-A286-B1293DD2EB5B01592215900
     * remark : 在评论中回复了我
     * type : 0
     * typeInfo : 1
     * createTime : 2020-06-03 15:37:31
     */

    private int articleId;
    private String nickname;
    private String headImg;
    private String remark;
    private int type;
    private int typeInfo;
    private String createTime;
}
