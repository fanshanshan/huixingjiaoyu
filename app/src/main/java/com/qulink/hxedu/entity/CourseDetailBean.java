package com.qulink.hxedu.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseDetailBean implements Serializable {


    /**
     * participant : {"value":"0"}
     * personal : {"contentId":1,"collectStatus":0,"purchaseStatus":0,"studyStatus":0,"videoId":null,"experienceStatus":0,"videoName":null,"videoDuration":null,"lastDuration":0}
     * detail : {"id":2,"curriculumImage":"2ebf3a0320e98a2eiWWX310MKVwaYnM7WD8NPCrvobwMStvS","curriculumDetail":"Java","popularStatus":1,"freeStatus":0,"payStatus":0,"specialStatus":0,"nickname":"泽元老师","headImg":"56832289-23C9-45D4-BF3B-3C8A322C4A0001591330112","intro":"LPL专业解说，神级预测，游泳的麦当劳","curriculumPrice":0,"vipPrice":0,"curriculumDuration":500,"chargeType":2}
     */

    private ParticipantBean participant;
    private PersonalBean personal;
    private DetailBean detail;

    @NoArgsConstructor
    @Data
    public static class ParticipantBean implements Serializable{
        /**
         * value : 0
         */

        private String value;
    }

    @NoArgsConstructor
    @Data
    public static class PersonalBean implements Serializable{
        /**
         * contentId : 1
         * collectStatus : 0
         * purchaseStatus : 0
         * studyStatus : 0
         * videoId : null
         * experienceStatus : 0
         * videoName : null
         * videoDuration : null
         * lastDuration : 0
         */

        private int contentId;
        private int collectStatus;
        private int purchaseStatus;
        private int studyStatus;
        private String videoId;
        private int experienceStatus;
        private String videoName;
        private String videoDuration;
        private int lastDuration;
    }

    @NoArgsConstructor
    @Data
    public static class DetailBean implements Serializable{
        /**
         * id : 2
         * curriculumImage : 2ebf3a0320e98a2eiWWX310MKVwaYnM7WD8NPCrvobwMStvS
         * curriculumDetail : Java
         * popularStatus : 1
         * freeStatus : 0
         * payStatus : 0
         * specialStatus : 0
         * nickname : 泽元老师
         * headImg : 56832289-23C9-45D4-BF3B-3C8A322C4A0001591330112
         * intro : LPL专业解说，神级预测，游泳的麦当劳
         * curriculumPrice : 0.0
         * vipPrice : 0.0
         * curriculumDuration : 500
         * chargeType : 2
         */

        private int id;
        private String curriculumImage;
        private String curriculumName;
        private String curriculumDetail;
        private int popularStatus;
        private int freeStatus;
        private int payStatus;
        private int specialStatus;
        private String nickname;
        private String headImg;
        private String intro;
        private double curriculumPrice;
        private double vipPrice;
        private int curriculumDuration;
        private int chargeType;
    }
}
