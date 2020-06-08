package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CourseDetailBean {
    /**
     * id : 1
     * curriculumImage : RTpcZmlsZVx1c2VySW1hZ2Vc5YGl6LqrLkpQRw==.jpg
     * curriculumDetail : 大家来欣赏短视频
     * popularStatus : 1
     * freeStatus : 0
     * payStatus : 1
     * specialStatus : 1
     * nickname : 泽元老师
     * headImg : RTpcZmlsZVx1c2VySW1hZ2Vc6IOM5b2xLkpQRw==.jpg
     * curriculumPrice : 100.0
     * vipPrice : 50.0
     */

    private int id;
    private String curriculumImage;
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


    public boolean isFree(){
        if(getPayStatus()==0){
            return true;
        }
        return false;
    }
     public boolean isVip(){
        if(getPayStatus()==0){
            return true;
        }
        return false;
    }


}
