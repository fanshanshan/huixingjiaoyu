package com.qulink.hxedu.util;

public class ImageUtils {
    public static String splitImgUrl(String prefix,String name){
        if(!prefix.contains("http")){
            prefix = "http://"+prefix;
        }
        return prefix+"/"+name;
    }
}
