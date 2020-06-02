package com.qulink.hxedu.util;

import com.qulink.hxedu.R;

public class ViewUtils {
    public static int getLevelBgByLevel(int level){
        int result = 0;
        if(level>=140){
            result = R.drawable.level8;
        }else if(level>=120){
            result = R.drawable.level7;
        }else if(level>=100){
            result = R.drawable.level6;
        }else if(level>=80){
            result = R.drawable.level5;
        }else if(level>=60){
            result = R.drawable.level4;
        }else if(level>=40){
            result = R.drawable.level3;
        }else if(level>=20){
            result = R.drawable.level2;
        }else {
            result = R.drawable.level1;
        }
        return result;
    }
}
