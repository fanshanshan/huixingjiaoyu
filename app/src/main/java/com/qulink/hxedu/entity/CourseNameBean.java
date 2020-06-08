package com.qulink.hxedu.entity;

import java.util.List;

public class CourseNameBean {
    /**
     * classify : {"id":2,"value":"健康系列"}
     * tags : [{"id":2,"value":"健康短视频"}]
     */

    private ClassifyBean classify;
    private List<TagsBean> tags;

    public ClassifyBean getClassify() {
        return classify;
    }

    public void setClassify(ClassifyBean classify) {
        this.classify = classify;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class ClassifyBean {
        /**
         * id : 2
         * value : 健康系列
         */

        private int id;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class TagsBean {
        /**
         * id : 2
         * value : 健康短视频
         */

        private int id;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
