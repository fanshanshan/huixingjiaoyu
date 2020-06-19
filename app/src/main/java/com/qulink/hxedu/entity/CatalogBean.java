package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CatalogBean {
    /**
     * chapterId : 1
     * chapterName : 雕像鉴赏
     * videoId : 1
     * videoName : 雕像
     * experienceStatus : 1
     * studyStatus : 0
     */

    private boolean isPlaying;
    private int chapterId;
    private String chapterName;
    private int videoId;
    private String videoName;
    private int experienceStatus;
    private int studyStatus;
    private VideoInfo videoInfo;
}
