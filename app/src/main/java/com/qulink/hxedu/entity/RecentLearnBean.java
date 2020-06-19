package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecentLearnBean {
    private List<RecentSevenDaysBean> today;
    private List<RecentSevenDaysBean> recentSevenDays;

    @NoArgsConstructor
    @Data
    public static class RecentSevenDaysBean {
        /**
         * scheduleId : 1
         * curriculumId : 1
         * contentId : 2
         * videoId : 2
         * curriculumName : Java生产环境下性能监控与调优详解
         * curriculumImage : img1.sycdn.imooc.com/szimg/5b384772000132c405400300-500-284.jpg
         * watchedTime : 1
         * curriculumType : 2
         */
        private boolean isCheck;
        private int scheduleId;
        private int curriculumId;
        private int contentId;
        private int videoId;
        private String curriculumName;
        private String curriculumImage;
        private int watchedTime;
        private int curriculumType;
    }
}
