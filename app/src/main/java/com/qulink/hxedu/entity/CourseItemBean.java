package com.qulink.hxedu.entity;

public class CourseItemBean {

    /**
     * id : 1
     * classifyName : 智慧系列
     * tagSort : 1
     * classifyImage : RTpcZmlsZVx1c2VySW1hZ2Vc5pSA55m76ICFLkpQRw==.jpg
     */

    private int id;
    private String classifyName;
    private int tagSort;
    private String classifyImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public int getTagSort() {
        return tagSort;
    }

    public void setTagSort(int tagSort) {
        this.tagSort = tagSort;
    }

    public String getClassifyImage() {
        return classifyImage;
    }

    public void setClassifyImage(String classifyImage) {
        this.classifyImage = classifyImage;
    }

}
