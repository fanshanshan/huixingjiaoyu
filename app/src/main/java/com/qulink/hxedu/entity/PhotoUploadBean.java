package com.qulink.hxedu.entity;

/**
 * Created by Administrator on 2018/9/11 0011.
 */

public class PhotoUploadBean {
   private String localPath;
   private String qiniuPath;

    public PhotoUploadBean() {
    }

    public PhotoUploadBean(String localPath, String qiniuPath) {
        this.localPath = localPath;
        this.qiniuPath = qiniuPath;
    }

    public String getLocalPath() {

        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }
}
