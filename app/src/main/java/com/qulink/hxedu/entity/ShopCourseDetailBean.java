package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ShopCourseDetailBean {
    /**
     * id : 1
     * curriculumId : 1
     * curriculumName : 短视频鉴赏
     * curriculumImage : 2ebf3a0320e98a2eiWWX310MKVwaYnM7WD8NPCrvobwMStvS
     * integration : 300
     * payAmount : 200.0
     * vipAmount : 100.0
     * exchangeType : 2
     * nickname : 泽元老师
     * headImg : 56832289-23C9-45D4-BF3B-3C8A322C4A0001591330112
     * intro : LPL专业解说，神级预测，游泳的麦当劳
     * curriculumDetail : 大家来欣赏短视频
     * exchangeQuantity : 1
     * isExchanged : 0
     */

    private int id;
    private int curriculumId;
    private String curriculumName;
    private String curriculumImage;
    private int integration;
    private double payAmount;
    private double vipAmount;
    private int exchangeType;
    private String nickname;
    private String headImg;
    private String intro;
    private String curriculumDetail;
    private int exchangeQuantity;
    private int isExchanged;
}
