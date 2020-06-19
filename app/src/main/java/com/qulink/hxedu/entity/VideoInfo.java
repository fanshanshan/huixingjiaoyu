package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VideoInfo {
    /**
     * videoName : 2-4 演示内存溢出
     * videoUrl : http://huixing-pub-assets.kuaiyunma.com/2-4%20%E6%BC%94%E7%A4%BA%E5%86%85%E5%AD%98%E6%BA%A2%E5%87%BA.mp4
     * videoTime : http://huixing-pub-assets.kuaiyunma.com/2-4%20%E6%BC%94%E7%A4%BA%E5%86%85%E5%AD%98%E6%BA%A2%E5%87%BA.mp4
     * videoSize :
     */

    private String videoName;
    private String videoUrl;
    private String videoTime;
    private String videoSize;
}
