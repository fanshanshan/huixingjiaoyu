package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveClassfyBean {
    /**
     * id : 1
     * title : Java
     * orderNum : 1
     * status : 1
     * createTime : 2020-06-04 10:12:00
     */

    private boolean isCheck;
    private int id;
    private String title;
    private int orderNum;
    private int status;
    private String createTime;
}
