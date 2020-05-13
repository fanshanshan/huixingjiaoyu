package com.qulink.hxedu.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {
    public static double getScreenWidth(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        Activity activity = (Activity)context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
