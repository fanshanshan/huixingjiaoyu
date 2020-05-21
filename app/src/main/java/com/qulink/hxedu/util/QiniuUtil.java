package com.qulink.hxedu.util;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

public class QiniuUtil {
    private static QiniuUtil instance;

    public static void setInstance(QiniuUtil instance) {
        QiniuUtil.instance = instance;
    }

    public static UploadManager getUploadManager() {
        return uploadManager;
    }

    public static void setUploadManager(UploadManager uploadManager) {
        QiniuUtil.uploadManager = uploadManager;
    }

    private static UploadManager uploadManager ;
    public static UploadManager getInstance(){
        if(uploadManager==null){
            Configuration config = new Configuration.Builder()
                    //.xxxx()
                    .dns(null)
                    //.yyyy()
                    .build();
            uploadManager = new UploadManager(config, 3);
        }
        return  uploadManager;
    }
}
