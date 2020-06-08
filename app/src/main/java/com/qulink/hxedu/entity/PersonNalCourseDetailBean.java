package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PersonNalCourseDetailBean {
    /**
     * collectStatus : 1
     * purchaseStatus : 2
     * studyStatus : 1
     * videoId : 1
     * experienceStatus : 0
     * videoName : 雕像
     * videoDuration : 00:10
     * lastDuration : 00:00
     */

    private int collectStatus;
    private int purchaseStatus;
    private int studyStatus;
    private int videoId;
    private int experienceStatus;
    private String videoName;
    private String videoDuration;
    private String lastDuration;
}
