package com.qulink.hxedu.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SystemNoticeBean {
    /**
     * records : [{"id":1,"title":"更新测试111","type":0,"unread":0},{"id":2,"title":"测试活动","type":1,"unread":0},{"id":3,"title":"测试公告","type":1,"unread":0}]
     * total : 3
     * size : 8
     * current : 1
     * orders : []
     * hitCount : false
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean hitCount;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;
    private List<?> orders;

    @NoArgsConstructor
    @Data
    public static class RecordsBean implements Serializable {
        /**
         * id : 1
         * title : 更新测试111
         * type : 0
         * unread : 0
         */

        private int id;
        private String title;
        private int type;
        private int unread;
    }
}
