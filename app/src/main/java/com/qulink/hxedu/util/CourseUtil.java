package com.qulink.hxedu.util;

public class CourseUtil {
    public static boolean isVipSpecial(int status){
        if(status==0){
            return false;
        }
        return true;
    }
    public static boolean isOk(int status){
        if(status==0){
            return false;
        }
        return true;
    }
}
