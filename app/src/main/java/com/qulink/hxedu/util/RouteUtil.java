package com.qulink.hxedu.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class RouteUtil {

    public static void startNewActivity(Activity context, Intent intent){
        context.startActivity(intent);
    }
    public static void startNewActivityAndResult(Activity context, Intent intent,int startCode){
        context.startActivityForResult(intent,startCode);
    }
}
